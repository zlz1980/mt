/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.init.utils;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.cache.server.init.model.FlowUnitStack;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.CaseModel;
import com.nantian.nbp.flowengine.model.DoWhileModel;
import com.nantian.nbp.flowengine.model.IfModel;
import com.nantian.nbp.flowengine.model.IfNotModel;
import com.nantian.nbp.flowengine.model.ProcMode;
import com.nantian.nbp.flowengine.model.SwitchModel;
import com.nantian.nbp.flowengine.model.TryModel;
import com.nantian.nbp.flowengine.model.WhileModel;
import com.nantian.nbp.utils.SpringContextUtils;

import java.util.LinkedHashMap;

import static com.nantian.nbp.cache.server.api.Constants.FLOW_BEK;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_BREAK;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_CASE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_CATCH;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_DEFAULT;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_DO;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_DO_WHILE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_ELSE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_ENDIF;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_CASE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_IFNOT;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_PROC;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_TRY;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_WHILE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_EXIT;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_IF;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_IFNOT;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_PROC;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_RETURN;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_SWITCH;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_TEMP;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_TRY;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_WHILE;

public class InitParseUtils {

	/**
	 * 解析结束语句
	 * @param stack 语法栈
	 * @param flowUnit 流程单元
	 * @return 语法块流程
	 */
	public static AstList parseEndStack(FlowUnitStack stack, FlowUnit flowUnit){
		 // 返回结果
		 AstList modelList= new AstList();
		 // 临时分支
		 AstList tmpList= new AstList();
		 // else分支
		 AstList elseBranch= new AstList();
		 // catch分支
		 AstModel catchUnit = null;
		 AstList catchBranch= new AstList();
		 // case分支
		 LinkedHashMap<String,CaseModel> caseBranch= new LinkedHashMap<>();

		 String endAtom = flowUnit.getAtomTranCode();
		 int endStep = flowUnit.getAtomTranNo();
		 String beginAtom = getBeginAtom(endAtom);
		 while (!stack.isEmpty()){
			 boolean structMatch = stack.isLastMatch();
			 AstModel model = stack.pop();
			 FlowUnit unit = model.getUnit();
			 String atomTranCode = unit.getAtomTranCode();
			 // 中间类型Else节点
			 if (FLOW_ELSE.equals(atomTranCode)){
				 elseBranch.addAll(tmpList);
				 tmpList.clear();
			 // 中间类型Catch节点
			 }else if (FLOW_CATCH.equals(atomTranCode)){
				 catchUnit = model;
				 catchBranch.addAll(tmpList);
				 tmpList.clear();
			 // 中间类型Case和Default节点
			 }else if(FLOW_CASE.equals(atomTranCode)||FLOW_DEFAULT.equals(atomTranCode)){
				 CaseModel caseModel=new CaseModel();
				 if(!tmpList.isEmpty()){
				     String lastAtom=tmpList.get(tmpList.size()-1).getUnit().getAtomTranCode();
				     if(FLOW_BREAK.equals(lastAtom)){
					     caseModel.setBreak(true);
					     tmpList.remove(tmpList.size()-1);
				     }else{
					    caseModel.setBreak(false);
				     }
				 }
				 if (FLOW_CASE.equals(atomTranCode)){
					 caseModel.setValue(unit.getAtomTranParam());
				 }else{
					 caseModel.setValue(CaseModel.DEFAULT);
				 }
				 caseModel.setType(ASTType.CASE);
				 caseModel.setUnit(unit);
				 caseModel.setCaseList(new AstList(tmpList));
				 caseBranch.put(caseModel.getValue(),caseModel);
				 tmpList.clear();
			 }else if (atomTranCode.equals(beginAtom)){
				 // if结构体
				 if (FLOW_IF.equals(beginAtom) && !structMatch){
					 addIfMode(stack,modelList,unit,tmpList,elseBranch,endStep);
					 break;
				 }
				 // try结构体
				 if (FLOW_TRY.equals(beginAtom) && !structMatch){
					 addTryMode(stack,modelList,unit,tmpList,catchUnit,catchBranch,endStep);
					 break;
				 }
				 // if-not结构体
				 if(FLOW_IFNOT.equals(beginAtom) && !structMatch){
					 addIfNotMode(stack,modelList,unit,tmpList);
					 break;
				 }
				 // do-while结构体
				 if (FLOW_DO.equals(beginAtom) && !structMatch){
					 addDoWhileMode(stack,modelList,unit,tmpList);
					 break;
				 }
				 // while结构体
				 if (FLOW_WHILE.equals(beginAtom) && !structMatch){
					 addWhileMode(stack,modelList,unit,tmpList,endStep);
					 break;
				 }
				 // switch结构体
				 if (FLOW_SWITCH.equals(beginAtom)  && !structMatch) {
					 addSwitchMode(stack, modelList, unit, caseBranch,endStep);
					 break;
				 }
				 // proc结构体
				 if (FLOW_PROC.equals(beginAtom) && !structMatch){
					 addProcMode(stack,modelList,unit,tmpList,endStep);
					 break;
				 }
				 tmpList.add(0, model);
			 }else {
				 tmpList.add(0, model);
			 }

		 }
		 return modelList;
	 }
	 
	 public static String getBeginAtom(String atomTran){
		switch (atomTran){
			case FLOW_ENDIF:
				return FLOW_IF;
			case FLOW_END_TRY:
				return FLOW_TRY;
			case FLOW_END_IFNOT:
				return FLOW_IFNOT;
			case FLOW_END_WHILE:
				return FLOW_WHILE;
			case FLOW_DO_WHILE:
				return FLOW_DO;
			case FLOW_END_CASE:
				return FLOW_SWITCH;
			case FLOW_END_PROC:
				return FLOW_PROC;
			default:
				return null;
		}
	 }

	public static AstModel createUnitAstModel(FlowUnit flowUnit) {
		AstModel model = new AstModel();
		String atomTranCode = flowUnit.getAtomTranCode();
		switch (atomTranCode){
			case FLOW_EXIT:
				model.setType(ASTType.EXIT);
				break;
			case FLOW_RETURN:
				model.setType(ASTType.RETURN);
				break;
			case FLOW_BEK:
				model.setType(ASTType.BEK);
				break;
			case FLOW_TEMP:
				model.setType(ASTType.TEMP);
				break;
			/* TRY语句会走外层 START_AST_SET的判断分支，下面的FLOW_TRY分支实际无机会执行 */
			case FLOW_TRY:
				model.setType(ASTType.TRY);
				break;
			case FLOW_CATCH:
				model.setType(ASTType.CATCH);
				break;
			case FLOW_ELSE:
				model.setType(ASTType.ELSE);
				break;
			case FLOW_CASE:
				model.setType(ASTType.CASE);
				break;
			case FLOW_BREAK:
				model.setType(ASTType.BREAK);
				break;
			case FLOW_DEFAULT:
				model.setType(ASTType.DEFAULT);
				break;
			default:
				model.setType(ASTType.COMM);
				try {
					AtomService atomService = SpringContextUtils.getBean(atomTranCode,AtomService.class);
					model.setAtomService(atomService);
				}catch (Exception e){
					throw new RuntimeException(String.format("%s,AtomService[%s] is not support",flowUnit,atomTranCode),e);
				}
		}
		model.setUnit(flowUnit);
		return model;
	}

	private static void addIfMode(FlowUnitStack stack, AstList modelList, FlowUnit unit,
								  AstList tmpList,AstList elseBranch, int endStep){
		IfModel ifModel=new IfModel();
		ifModel.setType(ASTType.IF);
		ifModel.setUnit(unit);
		ifModel.setIfBranch(tmpList);
		ifModel.setElseBranch(elseBranch);
		ifModel.setEndStep(endStep);
		if(stack.isEmpty()){
			modelList.add(ifModel);
		}else{
			stack.push(ifModel);
			stack.setLastMatch(true);
		}
	}

	private static void addTryMode(FlowUnitStack stack,AstList modelList,FlowUnit unit,
								   AstList tmpList,AstModel catchUnit,AstList catchBranch,int endStep){
		TryModel tryModel=new TryModel();
		tryModel.setType(ASTType.TRY);
		tryModel.setUnit(unit);
		tryModel.setTryBranch(tmpList);
		tryModel.setCatchUnit(catchUnit);
		tryModel.setCatchBranch(catchBranch);
		tryModel.setEndStep(endStep);
		if(stack.isEmpty()){
			modelList.add(tryModel);
		}else{
			stack.push(tryModel);
			stack.setLastMatch(true);
		}
	}

	@Deprecated
	private static void addIfNotMode(FlowUnitStack stack,AstList modelList,FlowUnit unit,
									 AstList tmpList){
		IfNotModel ifNotModel=new IfNotModel();
		ifNotModel.setType(ASTType.IFNOT);
		ifNotModel.setUnit(unit);
		ifNotModel.setIfNotBranch(tmpList);
		if(stack.isEmpty()){
			modelList.add(ifNotModel);
		}else{
			stack.push(ifNotModel);
			stack.setLastMatch(true);
		}
	}

	@Deprecated
	private static void addDoWhileMode(FlowUnitStack stack,AstList modelList,FlowUnit unit,
									   AstList tmpList){
		DoWhileModel doWhileModel=new DoWhileModel();
		doWhileModel.setType(ASTType.DOWHILE);
		doWhileModel.setUnit(unit);
		doWhileModel.setLoop(tmpList);
		if(stack.isEmpty()){
			modelList.add(doWhileModel);
		}else{
			stack.push(doWhileModel);
			stack.setLastMatch(true);
		}
	}

	private static void addWhileMode(FlowUnitStack stack,AstList modelList,FlowUnit unit,
									 AstList tmpList, int endStep){
		WhileModel whileModel=new WhileModel();
		whileModel.setType(ASTType.WHILE);
		whileModel.setUnit(unit);
		whileModel.setLoop(tmpList);
		whileModel.setEndStep(endStep);
		if(stack.isEmpty()){
			modelList.add(whileModel);
		}else{
			stack.push(whileModel);
			stack.setLastMatch(true);
		}
	}

	private static void addSwitchMode(FlowUnitStack stack,AstList modelList,FlowUnit unit,
									  LinkedHashMap<String,CaseModel> caseBranch, int endStep){
		SwitchModel switchModel=new SwitchModel();
		switchModel.setType(ASTType.SWITCH);
		switchModel.setUnit(unit);
		switchModel.setCaseBranch(new LinkedHashMap<>(caseBranch));
		switchModel.setEndStep(endStep);
		caseBranch.clear();
		if(stack.isEmpty()){
			modelList.add(switchModel);
		}else{
			stack.push(switchModel);
			stack.setLastMatch(true);
		}
	}

	private static void addProcMode(FlowUnitStack stack,AstList modelList,FlowUnit unit,
									AstList tmpList, int endStep){
		ProcMode procMode =new ProcMode();
		procMode.setType(ASTType.PROC);
		procMode.setUnit(unit);
		procMode.setProcList(tmpList);
		procMode.setEndStep(endStep);
		if(stack.isEmpty()){
			modelList.add(procMode);
		}else{
			stack.push(procMode);
			stack.setLastMatch(true);
		}
	}
}

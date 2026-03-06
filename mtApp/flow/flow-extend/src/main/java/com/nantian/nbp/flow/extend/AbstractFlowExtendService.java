package com.nantian.nbp.flow.extend;


import com.nantian.nbp.base.model.Flow;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowPara;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.TranCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 */
public abstract class AbstractFlowExtendService{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFlowExtendService.class);

    private static final String FLAG_TRUE = "Y";

    private final Map<FlowKey,Flow> builtParentFlowKeyMap = new HashMap<>();


    public Flow buildFlowByDb(FlowKey flowKey){
        TranCode tranCode = findTranCode(flowKey);
        return buildFlowByDb(tranCode);
    }

    public Flow buildFlowByDb(TranCode tranCode){
        return buildFlow(tranCode,false);
    }

    public Flow buildFlowByCache(FlowKey flowKey){
        TranCode currentTranCode = findTranCode(flowKey);
        return buildFlowByCache(currentTranCode);
    }

    public Flow buildFlowByCache(TranCode currentTranCode){
        return buildFlow(currentTranCode,true);
    }

    private Flow buildFlow(TranCode currentTranCode, boolean enableCache){
        // 获取继承编号
        String extendCode = currentTranCode.getExtendCode();
        LOGGER.trace("extendCode [{}]",extendCode);
        if (Objects.nonNull(currentTranCode.getParentFlowKey())) {
            Flow parentFlow;
            FlowKey parentFlowKey = currentTranCode.getParentFlowKey();
            if(enableCache && builtParentFlowKeyMap.containsKey(parentFlowKey)){
                parentFlow = builtParentFlowKeyMap.get(parentFlowKey);
            }else {
                // 根据父流程业务种类、内部交易码获取父流程信息
                TranCode parentInfo = findTranCode(parentFlowKey);
                if (Objects.isNull(parentInfo)) {
                    throw new RuntimeException(String.format("currentFlow[%s_%s],parentFlow[%s] is null",currentTranCode.getBusiType(),
                            currentTranCode.getTranCode(),extendCode));
                }
                // 获取父流程的继承标识，如果不为空获取父流程信息，递归处理;为空，根据交易信息获取流程各步骤信息
                if (Objects.nonNull(parentInfo.getParentFlowKey())) {
                    parentFlow = buildFlow(parentInfo,enableCache);
                } else {
                    parentFlow = getFlowByTranCode(parentInfo);
                }
            }
            // 获取子流程参数
            Flow currentFlow = getFlowByTranCode(currentTranCode);
            Flow builtFlow = buildNewFlowList(parentFlow, currentFlow,currentTranCode);
            // 临时缓存已经构建的父流程
            if(enableCache){
                builtParentFlowKeyMap.put(builtFlow.getKey(),builtFlow);
            }
            return builtFlow;
        } else {
            return getFlowByTranCode(currentTranCode);
        }
    }

    /**
     * 构建新流程信息
     * @param parentFlow 父流程信息
     * @param currentFlow 子流程信息
     * @return List<FlowUnit>
     */
    private Flow buildNewFlowList (Flow parentFlow ,Flow currentFlow,TranCode currentTranCode){
        List<FlowUnit> parentFlowStepList = parentFlow.getStepList();
        List<FlowUnit> currentFlowStepList = currentFlow.getStepList();

        // 将子流程按map结构拼接，key为作用域
        Map<String,FlowUnit> currentFlowMap = getFlowUnitMap(currentFlowStepList);
        // 如果继承的子流程为空，直接返回父流程
        if(CollectionUtils.isEmpty(currentFlowMap)){
            return new Flow(currentFlow.getKey(),parentFlow.getStepList());
        }

        List<FlowUnit> newFlowStepList = new LinkedList<>(parentFlowStepList);
        String flowAscope;
        FlowUnit sourceFlowUnit;
        // 遍历父流程，判断作用域是否相同，相同则进行替换
        for(int i=0,size=parentFlowStepList.size();i<size;i++ ){
            sourceFlowUnit = parentFlowStepList.get(i);
            flowAscope = sourceFlowUnit.getAscope();
            if(StringUtils.hasText(flowAscope) && currentFlowMap.containsKey(flowAscope)){
                newFlowStepList.set(i, updateAtomTranNo(sourceFlowUnit,currentFlowMap.get(flowAscope)));
                currentFlowMap.remove(flowAscope);
            }
        }
        if(!currentFlowMap.isEmpty()){
            throw new RuntimeException(String.format("childFlow[%s_%s],parentFlow[%s] has diff step[%s]",currentTranCode.getBusiType(),
                    currentTranCode.getTranCode(),currentTranCode.getExtendCode(),currentFlowMap.keySet()));
        }
        return new Flow(currentFlow.getKey(),newFlowStepList);
    }

    private Map<String,FlowUnit> getFlowUnitMap(List<FlowUnit> flowUnitList){
        if(CollectionUtils.isEmpty(flowUnitList)){
            return null;
        }
        Map<String,FlowUnit> childFlowMap = new HashMap<>(flowUnitList.size());
        for(FlowUnit flowUtil : flowUnitList){
            childFlowMap.put(flowUtil.getAscope(), flowUtil);
        }
        return childFlowMap;
    }

    /**
     * 替换对应步骤
     * @param sourceFlowUnit 原流程单元
     * @param flowUtil 流程单元
     * @return FlowUnit
     */
    private FlowUnit updateAtomTranNo(FlowUnit sourceFlowUnit,FlowUnit flowUtil){
        // 步骤编号
        Integer atomtranNo = sourceFlowUnit.getAtomTranNo();
        // 原原子交易编号
        String sourceAtomTranCode = sourceFlowUnit.getAtomTranCode();
        // 新流程步骤信息
        flowUtil.setAtomTranNo(atomtranNo);
        // 是否继承父类参数标识
        String extendParaFlag = flowUtil.getExtendParaFlag();
        if(!Objects.equals(extendParaFlag,FLAG_TRUE)){
            return flowUtil;
        }
        String atomTranCode = flowUtil.getAtomTranCode();
        // 新步骤增强处理参数
        List<FlowPara> flowParaList = flowUtil.getFlowParaList();
        if(CollectionUtils.isEmpty(flowParaList)){
            flowParaList = new LinkedList<>();
            flowUtil.setFlowParaList(flowParaList);
        }
        // 原步骤增强处理参数
        List<FlowPara> sourceFlowParaList = sourceFlowUnit.getFlowParaList();
        // 如果原子交易编号相同，继承父类参数,父类参数放置在前面
        if(!Objects.equals(sourceAtomTranCode,atomTranCode)){
            throw new RuntimeException(String.format("FlowUnit参数继承时，父子步骤atomTranCode不匹配,父[%s],子[%s]",sourceAtomTranCode,atomTranCode));
        }
        if(!CollectionUtils.isEmpty(sourceFlowParaList)){
            flowParaList.addAll(0, sourceFlowParaList);
        }
        flowUtil.initParamList();
        return flowUtil;
    }

    private Flow getFlowByTranCode(TranCode tranCode){
        FlowKey flowKey = tranCode.getFlowKey();
        Flow flow = findOriginalFlowByKey(flowKey);
        if(Objects.isNull(flow)){
            throw new RuntimeException(String.format("Flow[%s_%s] not found",tranCode.getBusiType(),tranCode.getTranCode()));
        }
        return flow;
    }

    /**
     * 根据业务种类和内部交易码，获取内部交易信息
     * @param flowKey 流程主键
     * @return 内部交易信息
     */
    public abstract TranCode findTranCode(FlowKey flowKey);

    /**
     * 通过内部交易获取原始未基层加工的流程信息
     * @param flowKey 流程主键
     * @return 流程信息
     */
    public abstract Flow findOriginalFlowByKey(FlowKey flowKey);

}

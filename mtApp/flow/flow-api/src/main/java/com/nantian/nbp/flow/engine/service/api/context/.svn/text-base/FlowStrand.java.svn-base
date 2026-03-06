/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.api.context;

import com.nantian.nbp.flowengine.model.ASTType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 流程总体，包括主流程或子流程
 * @author pengw at Jul 26, 2020 12:07:37 AM
 * @version V2.0
 */
@Component
public class FlowStrand extends Result {
    /** 主流程标识 */
    public final static String MAIN_PROCESS = "MAIN_PROCESS";
    /** 子流程标识 */
    public final static String SUB_PROCESS = "SUB_PROCESS";
    /** 流程类型，识别主/子流程 */
    private String flowType = MAIN_PROCESS;
    /** 流程ID，即业务种类+内部交易码 */
    private String id;
    /** 上下文对象 */
    private FeContext feContext;
    /** 父节点步骤号 */
    private String parentStep = "0";
    /** 当前步骤，用于日志输出 */
    private int curStep;
    /** 当前流程退出标志 */
    private boolean isExit = false;
    /** 当前流程返回标志 */
    private boolean isReturn = false;
    /** 当前流程跳出标志 */
    private boolean isBreak = false;
    /** 语法块，作用于当前节点是否处于While,Try等语境 */
    private List<ASTType> astBlock;

    public FlowStrand() {
    }

    public FlowStrand(String id, FeContext feContext) {
        this.id = id;
        this.feContext = feContext;
    }

    public FlowStrand(FeContext feContext) {
        this.feContext = feContext;
        this.id = feContext.getBizType() + feContext.getTranCode();
    }

    public FlowStrand(RetType retType, String id, FeContext feContext) {
        super(retType);
        this.id = id;
        this.feContext = feContext;
    }

    public FlowStrand(String id, String flowType, FeContext feContext) {
        this.flowType = flowType;
        this.feContext = feContext;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCurStep() {
        if (!Objects.equals(parentStep, "0")) {
            return parentStep + "." + curStep;
        }
        return String.valueOf(curStep);
    }

    public void setCurStep(int curStep) {
        this.curStep = curStep;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean isExit) {
        this.isExit = isExit;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public void setBreak(boolean aBreak) {
        isBreak = aBreak;
    }

    public FeContext getFeContext() {
        return feContext;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean isReturn) {
        this.isReturn = isReturn;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getFeTranCode() {
        return feContext.getFeTranCode();
    }

    public String getParentStep() {
        return parentStep;
    }

    public void setParentStep(String parentStep) {
        this.parentStep = parentStep;
    }

    public boolean containsAstBlock(ASTType astType) {
        if(Objects.isNull(astBlock)){
            return false;
        }
        return astBlock.contains(astType);
    }

    public void addAstBlock(ASTType astType) {
        if(Objects.isNull(astBlock)){
            astBlock = new ArrayList<>();
        }
        astBlock.add(astType);
    }

    public void removeAstBlock(ASTType astType) {
        if(Objects.nonNull(astBlock)){
            astBlock.remove(astType);
        }
    }

    public void addStep(String stepNo){
        getFeContext().getPbLog().addStep(stepNo);
    }
}

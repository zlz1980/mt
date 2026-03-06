/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.base.model;

import com.nantian.nbp.ev.MapUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 流程节点信息与流程增强处理参数、原子交易关系
 *
 * @author pengw at Jul 26, 2020 12:07:37 AM
 * @version V2.0
 */
public class FlowUnit implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String sagaParam;
    /**
     * 内部交易码
     */
    private String tranCode;
    /**
     * 业务种类
     */
    private String busiType;
    /**
     * 作用域
     */
    private String ascope;
    /**
     * 单参  解析后原子交易参数
     */
    private String atomTranParam;
    /**
     * 单参  原子交易参数
     */
    private String srcAtomTranParam;
    /**
     * 原子交易序号
     */
    private Integer atomTranNo;
    /**
     * 原子交易
     */
    private String atomTranCode;
    /**
     * 异常处理(N-不忽略 Y-忽略)
     */
    private String abnProcType;
    /**
     * 错误码
     */
    private String errCode;
    /**
     * 错误信息
     */
    private String errorInfo;
    /**
     * 引擎层使用获取多参
     */
    private List<String> paramList;
    /**
     * 增强处理参数，为节省内存引擎层不对此熟悉赋值，前段可使用
     */
    private List<FlowPara> flowParaList = new LinkedList<>();
    /**
     * 备注
     */
    private String note;
    /**
     * 是否继承父类参数标识N-不继承 Y-继承
     **/
    private String extendParaFlag;

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getSagaParam() {
        return sagaParam;
    }

    public void setSagaParam(String sagaParam) {
        this.sagaParam = sagaParam;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getAscope() {
        return ascope;
    }

    public void setAscope(String ascope) {
        this.ascope = ascope;
    }

    public String getAtomTranParam() {
        return atomTranParam;
    }

    public void setAtomTranParam(String atomTranParam) {
        this.atomTranParam = atomTranParam;
    }

    public Integer getAtomTranNo() {
        return atomTranNo;
    }

    public void setAtomTranNo(Integer atomTranNo) {
        this.atomTranNo = atomTranNo;
    }

    public String getAtomTranCode() {
        return atomTranCode;
    }

    public void setAtomTranCode(String atomTranCode) {
        this.atomTranCode = atomTranCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public void initParamList() {
        List<String> tmpParamList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(flowParaList)) {
            for (FlowPara flowPara : flowParaList) {
                tmpParamList.add(MapUtils.repElVal(flowPara.getAtomPara()));
            }
        }
        this.paramList = tmpParamList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExtendParaFlag() {
        return extendParaFlag;
    }

    public void setExtendParaFlag(String extendParaFlag) {
        this.extendParaFlag = extendParaFlag;
    }

    public String getAbnProcType() {
        return abnProcType;
    }

    public void setAbnProcType(String abnProcType) {
        this.abnProcType = abnProcType;
    }

    public List<FlowPara> getFlowParaList() {
        if (null == flowParaList) {
            this.flowParaList = new LinkedList<>();
        }
        return flowParaList;
    }

    public String getSrcAtomTranParam() {
        return srcAtomTranParam;
    }

    public void setFlowParaList(List<FlowPara> flowParaList) {
        this.flowParaList = flowParaList;
    }

    @Override
    public String toString() {
        return "FlowUnit{" + "busiType='" + busiType + '\'' + ", tranCode='" + tranCode + '\'' + ", atomTranNo=" + atomTranNo + ", ascope='" + ascope + '\'' + '}';
    }
}

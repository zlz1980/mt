package com.nantian.nbp.base.model.bo;

import com.nantian.nbp.base.model.FlowPara;
import com.nantian.nbp.base.model.FlowUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ Author wushengchao
 * @ Date 2025/4/14
 * 关键点：父子强转，要求原始数据必须为FlowUnitVo，引用态允许位父类
 **/
public class FlowUnitBo extends FlowUnit {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowUnitBo.class);

    private String oldAscope;

    private String atomTranName;

    private String areaNo;

    public FlowUnitBo(){

    }

    public FlowUnitBo(FlowUnit flowUnit){
        setFlowUnit(flowUnit);
    }

    public void setFlowUnit(FlowUnit flowUnit){
        setSagaParam(flowUnit.getSagaParam());
        setTranCode(flowUnit.getTranCode());
        setBusiType(flowUnit.getBusiType());
        setAscope(flowUnit.getAscope());
        setAtomTranParam(flowUnit.getAtomTranParam());
        setAtomTranNo(flowUnit.getAtomTranNo());
        setAtomTranCode(flowUnit.getAtomTranCode());
        setAbnProcType(flowUnit.getAbnProcType());
        setErrCode(flowUnit.getErrCode());
        setErrorInfo(flowUnit.getErrorInfo());
        setFlowParaList(flowUnit.getFlowParaList());
        setNote(flowUnit.getNote());
        setExtendParaFlag(flowUnit.getExtendParaFlag());
    }

    @Override
    public void setFlowParaList(List<FlowPara> flowParaList) {
        super.setFlowParaList(flowParaList);
    }

    public List<FlowPara> filterSelfList() {
        return getFlowParaList().stream()
                .filter(flowUnit -> getBusiType().equals(flowUnit.getBusiType())
                        && getTranCode().equals(flowUnit.getTranCode())).collect(Collectors.toList());
    }

    public FlowUnit toFlowUnit() {
        return this;
    }

    public static FlowUnit toFlowUnit(FlowUnitBo vo) {
        return vo;
    }

    public static FlowUnitBo fromFlowUnit(FlowUnit unit) {
        if(unit instanceof FlowUnitBo){
            return (FlowUnitBo) unit;
        }else {
            throw new IllegalArgumentException("unit is not FlowUnitVo type");
        }

    }

    public String getOldAscope() {
        return oldAscope;
    }

    public void setOldAscope(String oldAscope) {
        this.oldAscope = oldAscope;
    }

    public String getAtomTranName() {
        return atomTranName;
    }

    public void setAtomTranName(String atomTranName) {
        this.atomTranName = atomTranName;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

}

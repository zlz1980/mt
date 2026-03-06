package com.nantian.nbp.base.model.bo;

import com.nantian.nbp.base.model.Flow;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.TranCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ Author wushengchao
 * @ Date 2025/4/2
 * 修改思路：所需属性直接加，为空也不会消耗多少。
 * FlowExtendService中提供抽象属性获取方法，
 * 该抽象方法各平台不同实现。运行平台不需要则实现内容为返回空就行
 **/

public class FlowBo extends Flow {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowBo.class);

    private List<FlowUnitBo> unitList;

    private TranCode tran;

    private FlowExtApi api;

    public FlowBo() {
        super();
    }

    public FlowBo(FlowKey key) {
        super(key);
    }

    public FlowBo(FlowKey key, List<FlowUnitBo> stepList) {
        super(key);
        this.unitList = stepList;
    }

    public FlowBo(Flow flow, TranCode tran, FlowExtApi api) {
        super(flow.getKey(),flow.getStepList());
        this.unitList = toFlowUnitBoList(flow.getStepList());
        this.tran = tran;
        this.api = api;
    }

    /**
     * 仅适配父类方法
     */
    @Override
    public void setStepList(List<FlowUnit> stepList) {
        this.unitList = toFlowUnitBoList(stepList);
    }

    /**
     * 适配父类兼容，该列表仅读
     */
    @Override
    public List<FlowUnit> getStepList() {
        return toFlowUnitList(getUnitList());
    }


    /**
     * 获取流程步骤集合，防空get
     */
    public List<FlowUnitBo> getUnitList() {
        return Optional.ofNullable(this.unitList).orElseGet(() -> {
            this.unitList = new LinkedList<>();
            return this.unitList;
        });
    }

    /**
     * 只筛选出属于当前流程的所有步骤，创建新列表对象
     */
    public List<FlowUnitBo> filterSelfList() {
        return getUnitList().stream()
                .filter(flowUnit -> Objects.equals(getKey().getBusiType(),flowUnit.getBusiType())
                        && Objects.equals(getKey().getTranCode(),flowUnit.getTranCode()))
                .collect(Collectors.toList());
    }

    @Override
    public void addStep(FlowUnit step) {
        if (step instanceof FlowUnitBo) {
            unitList.add((FlowUnitBo) step);
        }
        throw new IllegalArgumentException("step must be instances of FlowUnitVo");
    }

    /**
     * 仅适配父类方法，不建议使用
     */
    @SuppressWarnings("unchecked")
    public static List<FlowUnit> toFlowUnitList(List<FlowUnitBo> unitList) {
        return (List<FlowUnit>) (List<?>) unitList;
    }

    /**
     * voList 转 stepList
     */
    @SuppressWarnings("unchecked")
    private List<FlowUnitBo> toFlowUnitBoList(List<FlowUnit> stepList) {
        if (stepList.stream().allMatch(FlowUnitBo.class::isInstance)) {
            return (List<FlowUnitBo>) (List<?>) stepList;
        } else {
            throw new IllegalArgumentException("FlowUnit is null or Original is not FlowUnitVo");
        }
    }

    public void setUnitList(List<FlowUnitBo> unitList) {
        this.unitList = unitList;
    }

    public TranCode getTran() {
        return tran;
    }

    public void setTran(TranCode tran) {
        this.tran = tran;
    }

    public FlowExtApi getApi() {
        return api;
    }

    public void setApi(FlowExtApi api) {
        this.api = api;
    }

}

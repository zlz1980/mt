package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.DecisionStep;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InitDecisionStepMapper {
    @Select("SELECT BUSITYPE,TRANCODE,ASCOPE FROM t_pb_flow where ATOMTRANCODE = 'base.DecisionTable'")
    List<DecisionStep> findAllDecisionStep();
}

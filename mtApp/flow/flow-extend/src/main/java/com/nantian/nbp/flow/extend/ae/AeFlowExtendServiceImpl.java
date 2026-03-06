package com.nantian.nbp.flow.extend.ae;

import com.nantian.nbp.base.model.Flow;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowPara;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.base.model.bo.FlowBo;
import com.nantian.nbp.base.model.bo.FlowUnitBo;
import com.nantian.nbp.flow.extend.AbstractFlowExtendService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AeFlowExtendServiceImpl extends AbstractFlowExtendService {

    private final SqlSessionTemplate sqlSessionTemplate;

    public AeFlowExtendServiceImpl(SqlSessionTemplate sqlSessionTemplate){
        sqlSessionTemplate.getConfiguration().addMapper(FlowExtendMapper.class);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public TranCode findTranCode(FlowKey key) {
        return sqlSessionTemplate.getMapper(FlowExtendMapper.class).findTranInfo(key);
    }

    @Override
    public Flow findOriginalFlowByKey(FlowKey key) {
        if (ObjectUtils.isEmpty(key)
            || ObjectUtils.isEmpty(key.getTranCode())
            || ObjectUtils.isEmpty(key.getBusiType())) {
            return null;
        }
        FlowExtendMapper mapper = sqlSessionTemplate.getMapper(FlowExtendMapper.class);
        List<FlowUnitBo> flowUnitList = mapper.findFlowUnitList(key);
        Map<String, LinkedList<FlowPara>> flowParamMap = fetchFlowParaResource(mapper, key, flowUnitList.size());
        flowUnitList.forEach(flowUnit -> flowUnit.setFlowParaList(flowParamMap.get(flowUnit.getAscope())));
        return new FlowBo(key, flowUnitList);
    }

    private Map<String, LinkedList<FlowPara>> fetchFlowParaResource(FlowExtendMapper mapper, FlowKey key, int flowUnitCount) {
        List<FlowPara> flowParamList = mapper.findFlowParaList(key);
        Map<String, LinkedList<FlowPara>> flowParamMap = new HashMap<>(flowUnitCount);
        String recordAscope = null;
        String currentAscope;
        LinkedList<FlowPara> linkedFlowParaList = null;
        for (FlowPara flowPara : flowParamList) {
            currentAscope = flowPara.getAscope();
            if (!Objects.equals(recordAscope, currentAscope)) {
                linkedFlowParaList = new LinkedList<>();
                flowParamMap.put(currentAscope, linkedFlowParaList);
            }
            recordAscope = currentAscope;
            if(Objects.nonNull(linkedFlowParaList)) {
                linkedFlowParaList.add(flowPara);
            }else {
                throw new RuntimeException("linkedFlowParaList is null,aScope:" + currentAscope);
            }

        }
        return flowParamMap;
    }
}

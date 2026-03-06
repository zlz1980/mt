package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.Flow;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowPara;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.FlowUnitKey;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitFlowMapper;
import com.nantian.nbp.cache.server.init.mapper.InitFlowParaMapper;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.flowengine.model.FlowContainer;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;

public class FlowContainerResourceCacheLoader extends ResourceCacheLoader<FlowContainer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowContainerResourceCacheLoader.class);
    private final SqlSessionTemplate sqlSessionTemplate;

    private final FbspFlowExtendServiceImpl flowExtendServiceImpl;

    public FlowContainerResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate,
            FbspFlowExtendServiceImpl flowExtendServiceImpl) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.flowExtendServiceImpl = flowExtendServiceImpl;
    }

    private Map<FlowUnitKey, LinkedList<FlowPara>> fetchFlowParaResource(int flowUnitCount) {
        List<FlowPara> flowParalist = sqlSessionTemplate.getMapper(InitFlowParaMapper.class).findFlowPara();
        if (flowParalist == null) {
            throw new RuntimeException(APP_CACHE_ERR_KEY + " null data list returned by findFlowPara from DB");
        }

        Map<FlowUnitKey, LinkedList<FlowPara>> flowParaRes = new HashMap<>(flowUnitCount);
        FlowUnitKey recordFlowUnitKey = null;
        FlowUnitKey currentFlowUnitKey;
        LinkedList<FlowPara> linkedFlowParaList = null;
        for (FlowPara flowPara : flowParalist) {
            currentFlowUnitKey = FlowUnitKey.of(flowPara.getBusiType(), flowPara.getTranCode(), flowPara.getAscope());
            if (!Objects.equals(recordFlowUnitKey, currentFlowUnitKey)) {
                linkedFlowParaList = new LinkedList<>();
                flowParaRes.put(currentFlowUnitKey, linkedFlowParaList);
            }
            recordFlowUnitKey = currentFlowUnitKey;
            //String atomPara = flowPara.getAtomPara();
            linkedFlowParaList.add(flowPara);
        }
        return flowParaRes;
    }

    private void buildFlowMapResource(Map<FlowKey, Flow> flowMap) {
        List<FlowUnit> allFlowUnitList = sqlSessionTemplate.getMapper(InitFlowMapper.class).findFlowUnit();
        if (allFlowUnitList == null) {
            throw new RuntimeException(APP_CACHE_ERR_KEY + " null data list returned by findFlowUnit from DB");
        }
        Map<FlowUnitKey, LinkedList<FlowPara>> flowParaRes = fetchFlowParaResource(allFlowUnitList.size());
        FlowKey recordFlowKey = null;
        FlowKey currentFlowKey;
        Flow flow = null;
        for (FlowUnit flowUnit : allFlowUnitList) {
            currentFlowKey = FlowKey.of(flowUnit.getBusiType(), flowUnit.getTranCode());
            if (!Objects.equals(recordFlowKey, currentFlowKey)) {
                flow = new Flow(currentFlowKey);
                flow.setStepList(new LinkedList<>());
                flowMap.put(currentFlowKey, flow);
            }
            recordFlowKey = currentFlowKey;
            List<FlowPara> flowParaList = flowParaRes.get(FlowUnitKey.of(flowUnit.getBusiType(), flowUnit.getTranCode(), flowUnit.getAscope()));
            flowUnit.setFlowParaList(flowParaList);
            String atomTranParam = flowUnit.getSrcAtomTranParam();
            flowUnit.setAtomTranParam(MapUtils.repElVal(atomTranParam));
            flowUnit.initParamList();
            flow.addStep(flowUnit);
        }
        buildExtendsFlow(flowMap);
    }

    private void buildExtendsFlow(Map<FlowKey, Flow> flowMap) {
        // 查询包含继承的流程
        List<TranCode> extendTranCodeList = findExtendTranCodeInfo();
        if (CollectionUtils.isEmpty(extendTranCodeList)) {
            return;
        }
        Flow newFlow;
        // 遍历tranCode,组装flowKey,获取对应流程
        for (TranCode tranCode : extendTranCodeList) {
            FlowKey flowKey = new FlowKey();
            flowKey.setBusiType(tranCode.getBusiType());
            flowKey.setTranCode(tranCode.getTranCode());
            newFlow = flowExtendServiceImpl.buildFlowByCache(tranCode);
            flowMap.put(flowKey, newFlow);
        }
    }

    private List<TranCode> findExtendTranCodeInfo() {
        // 查询包含继承的流程
        InitFlowMapper initFlowMapper = sqlSessionTemplate.getMapper(InitFlowMapper.class);
        return initFlowMapper.findExtendTranCodeInfo();
    }

    @Override
    public PbResourceCache<FlowContainer> initCache() {
        List<FlowKey> flowKeyList = initAllFlowKey();
        Map<FlowKey, Flow> flowMap = new HashMap<>(flowKeyList.size());
        flowExtendServiceImpl.setFlowMap(flowMap);
        buildFlowMapResource(flowMap);
        PbResourceCache<FlowContainer> resourceCache = new PbResourceCache<>(getResourceName(), flowKeyList.size());
        InitFlowContainerTask saveFlowContainerTask = new InitFlowContainerTask(flowKeyList, flowMap, resourceCache, flowExtendServiceImpl);
        saveFlowContainerTask.invoke();
        return resourceCache;
    }

    private List<FlowKey> initAllFlowKey() {
        return sqlSessionTemplate.getMapper(InitFlowMapper.class).findAllTranCodeKey();
    }
}

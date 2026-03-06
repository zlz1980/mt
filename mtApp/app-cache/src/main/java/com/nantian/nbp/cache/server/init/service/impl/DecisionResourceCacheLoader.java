/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.cache.server.init.service.impl;


import com.nantian.nbp.base.model.DecisionStep;
import com.nantian.nbp.base.model.Flow;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbCache;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitDecisionStepMapper;
import com.nantian.nbp.flowengine.model.FlowContainer;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_CONTAINER;
import static com.nantian.nbp.utils.Constants.PARAM_SPILT_FLAG;


/**
 * @author Administrator
 */
public class DecisionResourceCacheLoader extends ResourceCacheLoader<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecisionResourceCacheLoader.class);
    private final SqlSessionTemplate sqlSessionTemplate;
    private final Supplier<PbCache> pbCacheSupplier;

    public DecisionResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate,
            Supplier<PbCache> pbCacheSupplier) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.pbCacheSupplier = pbCacheSupplier;
    }

    @Override
    public PbResourceCache<Map<String, Object>> initCache() {
        List<DecisionStep> decisionSteps =
                sqlSessionTemplate.getMapper(InitDecisionStepMapper.class).findAllDecisionStep();
        PbResourceCache<Map<String, Object>> resourceCache = new PbResourceCache<>(getResourceName(), decisionSteps.size());
        for (DecisionStep decisionStep : decisionSteps) {
            buildDecisionTable(decisionStep, resourceCache);
        }
        return resourceCache;
    }

    private void buildDecisionTable(DecisionStep decisionStep, PbResourceCache<Map<String, Object>> resourceCache) {
        FlowKey flowKey = FlowKey.of(decisionStep.getBusiType(), decisionStep.getTranCode());
        FlowContainer flowContainer = getFlowContainer(flowKey);
        if (ObjectUtils.isEmpty(flowContainer)) {
            LOGGER.error(APP_CACHE_ERR_KEY + " FlowContainer is null, flowKey:{}", flowKey);
            throw new RuntimeException(String.format("FlowContainer is null, flowKey:[%s]", flowKey));
        } else {
            List<String> paramList = new LinkedList<>();
            Flow flow = flowContainer.getFlow();
            List<FlowUnit> stepList = flow.getStepList();
            for (FlowUnit unit : stepList) {
                if (unit.getAscope().equals(decisionStep.getaScope())) {
                    paramList = unit.getParamList();
                    break;
                }
            }
            if (ObjectUtils.isEmpty(paramList)) {
                LOGGER.error(APP_CACHE_ERR_KEY + " DecisionTable flowParaList is null [{}]", decisionStep);
            } else {
                Map<String, Object> atomParaMap = new HashMap<>();
                paramList.forEach(flowPara -> {
                    String[] params = StringUtils.delimitedListToStringArray(flowPara, PARAM_SPILT_FLAG);
                    atomParaMap.put(params[params.length - 1].replaceAll("'", ""), flowPara);
                });
                String key = decisionStep.getBusiType() + "_" + decisionStep.getTranCode() + "_" + decisionStep.getaScope();
                resourceCache.put(key, atomParaMap);
                LOGGER.info("DecisionTableName [{}] ", key);
            }
        }
    }

    public FlowContainer getFlowContainer(FlowKey flowKey) {
        return cache().get(FLOW_CONTAINER, flowKey.toString());
    }

    private PbCache cache() {
        return pbCacheSupplier.get();
    }
}


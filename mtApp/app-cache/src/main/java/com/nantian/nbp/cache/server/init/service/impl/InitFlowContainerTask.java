/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.cache.server.init.service.impl;


import com.nantian.nbp.base.model.Flow;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.model.FlowUnitStack;
import com.nantian.nbp.cache.server.init.utils.InitParseUtils;
import com.nantian.nbp.flow.extend.AbstractFlowExtendService;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.AsyncModuleModel;
import com.nantian.nbp.flowengine.model.DoWhileModel;
import com.nantian.nbp.flowengine.model.FlowContainer;
import com.nantian.nbp.flowengine.model.HandleModuleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.RecursiveAction;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_ASYNC_MODULE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_DO;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_DO_WHILE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_ENDIF;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_CASE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_IFNOT;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_PROC;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_TRY;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_END_WHILE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_IF;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_IFNOT;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_MODULE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_PROC;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_SWITCH;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_TRY;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_WHILE;
import static com.nantian.nbp.cache.server.api.Constants.TRAN_CODE_MSG_KEY;


/**
 * @author Administrator
 */
public class InitFlowContainerTask extends RecursiveAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitFlowContainerTask.class);
    /** 空对象 */
    private static final AstList EMPTY_AST_MODEL = new AstList();
    /** 开始控制语句 */
    private static final Set<String> START_AST_SET = new HashSet<String>(7) {{
        add(FLOW_IF);
        add(FLOW_IFNOT);
        add(FLOW_WHILE);
        add(FLOW_DO);
        add(FLOW_SWITCH);
        add(FLOW_TRY);
        add(FLOW_PROC);
    }};

    /** 结尾控制语句 **/
    private static final Set<String> END_AST_SET = new HashSet<String>(7) {{
        add(FLOW_ENDIF);
        add(FLOW_DO_WHILE);
        /* FLOW_END_CASE对应FLOW_SWITCH */
        add(FLOW_END_CASE);
        add(FLOW_END_IFNOT);
        add(FLOW_END_WHILE);
        add(FLOW_END_TRY);
        add(FLOW_END_PROC);
    }};

    /** 每一批次入的数据*/
    private final static int BATCH_SIZE = 300;
    private final static int INDEX = 3;

    private final List<FlowKey> list;
    /** 缓存接口 */
    private final PbResourceCache<FlowContainer> cache;

    private final Map<FlowKey, Flow> flowMap;
    /**  */
    private final AbstractFlowExtendService flowExtendServiceImpl;

    public InitFlowContainerTask(List<FlowKey> list, Map<FlowKey, Flow> flowMap,  PbResourceCache<FlowContainer> cache, AbstractFlowExtendService flowExtendServiceImpl) {
        this.list = list;
        this.cache = cache;
        this.flowMap = flowMap;
        this.flowExtendServiceImpl = flowExtendServiceImpl;
    }
    @Override
    protected void compute() {
        if (list.size() <= BATCH_SIZE) {
            putDataToCache(list);
        } else {
            int size = list.size();
            // 进行分组
            InitFlowContainerTask initCacheTask1 = new InitFlowContainerTask(list.subList(0, size / 2), flowMap, cache, flowExtendServiceImpl);
            InitFlowContainerTask initCacheTask2 = new InitFlowContainerTask(list.subList(size / 2, size), flowMap, cache, flowExtendServiceImpl);
            // 任务并发执行
            invokeAll(initCacheTask1, initCacheTask2);
        }
    }

    private void putDataToCache(List<FlowKey> flowKeyList) {
        for (FlowKey flowkey : flowKeyList) {
            parseFlowContainer(flowkey);
        }
    }

    private void parseFlowContainer(FlowKey flowKey) {
        LOGGER.info("FlowContainer-S [{}]", flowKey);
        Flow flow = flowMap.get(flowKey);
        if(Objects.isNull(flow)){
            LOGGER.warn("Flow[{}] is empty",flowKey);
            return;
        }
        FlowContainer flowContainer = new FlowContainer();
        flowContainer.setFlow(flow);
        // 构建语法树
        AstList astList = parseStruct(new FlowUnitStack(), flow, flowKey);
        flowContainer.setExecList(astList);
        LOGGER.info("FlowContainer-E [{}]", flowKey);
        cache.put(flowKey.toString(), flowContainer);
    }

    private AstList parseStruct(FlowUnitStack stack, Flow flow, FlowKey pFlowKey) {
        List<FlowUnit> flowUnitList = flow.getStepList();
        if (CollectionUtils.isEmpty(flowUnitList)) {
            LOGGER.warn("Flow[{}] stepList is empty",flow.getKey());
            return EMPTY_AST_MODEL;
        }
        AstList modelList = new AstList();
        for (FlowUnit flowUnit : flowUnitList) {
            String atomTranCode = flowUnit.getAtomTranCode();
            // 解析异步处理模块
            if (FLOW_ASYNC_MODULE.equals(atomTranCode)) {
                parseAsyncModule(stack, modelList, pFlowKey, flowUnit);
                // 解析处理模块
            } else if (FLOW_MODULE.equals(atomTranCode)) {
                parseHandleModule(stack, modelList, pFlowKey, flowUnit);
                // 语法块开始
            } else if (START_AST_SET.contains(atomTranCode)) {
                AstModel model = new AstModel();
                model.setUnit(flowUnit);
                stack.push(model);
                // 语法块结束
            } else if (END_AST_SET.contains(atomTranCode)) {
                AstList structList = InitParseUtils.parseEndStack(stack, flowUnit);
                if (!structList.isEmpty()) {
                    modelList.addAll(structList);
                }
                // 普通节点
            } else {
                AstModel model = InitParseUtils.createUnitAstModel(flowUnit);
                if (stack.isEmpty()) {
                    if (!modelList.isEmpty()) {
                        AstModel lastModel = modelList.get(modelList.size() - 1);
                        if (lastModel instanceof DoWhileModel) {
                            DoWhileModel doWhileModel = (DoWhileModel) lastModel;
                            if (doWhileModel.getCondition() == null) {
                                doWhileModel.setCondition(model);
                            } else {
                                modelList.add(model);
                            }
                        } else {
                            modelList.add(model);
                        }
                    } else {
                        modelList.add(model);
                    }
                } else {
                    if (!stack.isEmpty()) {
                        Boolean matchFlag = stack.isLastMatch();
                        AstModel lastModel = stack.pop();
                        if (lastModel instanceof DoWhileModel) {
                            DoWhileModel doWhileModel = (DoWhileModel) lastModel;
                            if (doWhileModel.getCondition() == null) {
                                doWhileModel.setCondition(model);
                                stack.push(doWhileModel);
                            } else {
                                stack.push(lastModel);
                                stack.setLastMatch(matchFlag);
                                stack.push(model);
                            }
                        } else {
                            stack.push(lastModel);
                            stack.setLastMatch(matchFlag);
                            stack.push(model);
                        }
                    } else {
                        stack.push(model);
                    }
                }
            }
        }
        return modelList;
    }

    private void parseHandleModule(FlowUnitStack stack, List<AstModel> modelList, FlowKey pFlowKey, FlowUnit flowUnit) {
        HandleModuleModel model = new HandleModuleModel();
        model.setType(ASTType.HANDLEMODULE);
        parseModuleModel(model, stack, modelList, pFlowKey, flowUnit);
    }

    private void parseAsyncModule(FlowUnitStack stack, List<AstModel> modelList, FlowKey pFlowKey, FlowUnit flowUnit) {
        AsyncModuleModel model = new AsyncModuleModel();
        model.setType(ASTType.ASYNMODULE);
        parseModuleModel(model, stack, modelList, pFlowKey, flowUnit);
    }

    private void parseModuleModel(HandleModuleModel model,FlowUnitStack stack, List<AstModel> modelList, FlowKey pFlowKey, FlowUnit flowUnit) {
        model.setUnit(flowUnit);
        String atomTranParam = flowUnit.getAtomTranParam();
        Integer atomTranNo = flowUnit.getAtomTranNo();
        FlowKey flowKey = new FlowKey(atomTranParam.substring(0, INDEX), atomTranParam.substring(INDEX));
        Flow childFlow = flowMap.get(flowKey);
        if (Objects.isNull(childFlow)) {
            String errInfo = String.format(APP_CACHE_ERR_KEY + " flow parse warn,parentFlow[%s] atomTranNo[%s] childFlow[%s] is null",
                    pFlowKey, atomTranNo,flowKey);
            LOGGER.error(errInfo);
            throw new RuntimeException(errInfo);
        }
        AstList handleList = parseStruct(new FlowUnitStack(), childFlow, pFlowKey);
        model.setChildrenList(handleList);
        // 获取tranCode信息
        TranCode tranCode = flowExtendServiceImpl.findTranCode(flowKey);
        if (Objects.isNull(tranCode)) {
            String errInfo = String.format(APP_CACHE_ERR_KEY + " flow parse warn,flow[%s] atomTranNo[%s] tranCode[%s] is null",
                    pFlowKey, atomTranNo, TRAN_CODE_MSG_KEY + flowKey);
            LOGGER.error(errInfo);
            throw new RuntimeException(errInfo);
        }
        model.setTranCode(tranCode);
        if (stack.isEmpty()) {
            modelList.add(model);
        } else {
            stack.push(model);
        }
    }

}


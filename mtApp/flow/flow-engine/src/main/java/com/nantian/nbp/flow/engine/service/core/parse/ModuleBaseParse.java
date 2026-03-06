/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.Api;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.flow.engine.service.api.util.json.valid.ValidJsonUtils;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.HandleModuleModel;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.nantian.nbp.cache.server.api.Constants.JSON_SCHEMA_FLAG_TRUE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.JSON_SCHEMA_FLAG;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.REQ_JSON_SCHEMA_TYPE;
import static com.nantian.nbp.flow.engine.service.api.ScopeValUnit.RESULT_KEY;

/**
 * 处理模块语法处理
 *
 * @author Administrator
 */
public class ModuleBaseParse extends BaseParseStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleBaseParse.class);

    @Override
    public AtomResult doHandle(FlowStrand flowStrand, AstModel model) {
        AtomResult atomResult = new AtomResult();
        if (model instanceof HandleModuleModel) {
            HandleModuleModel handleModuleModel = (HandleModuleModel) model;
            normalHandle(flowStrand, handleModuleModel, atomResult);
        } else {
            throwAstTypeException("HandleModuleModel", model.getType());
        }
        return atomResult;
    }

    private void normalHandle(FlowStrand flowStrand, HandleModuleModel handleModuleModel, AtomResult atomResult) {
        Long startTime = Timer.getStartTime();
        FlowUnit flowUnit = handleModuleModel.getUnit();
        String curStep = flowStrand.getCurStep();
        String atomTranParam = flowUnit.getAtomTranParam();
        LOGGER.info("##Step[{}] Execute HandleModule[{}]Begin##", curStep, atomTranParam);
        LOGGER.info("AtomTran note[{}]", flowUnit.getNote());
        FeContext feContext = flowStrand.getFeContext();
        ScopeValUnit inScopeValUnit = new ScopeValUnit();
        FlowParaUtils.setFlowParaValues(feContext, inScopeValUnit, flowUnit);
        /*基于jsonschema接口定义对输入进行格式校验*/
        checkReqJsonSchema(feContext, handleModuleModel, inScopeValUnit);
        // 获取处理模块内步骤list
        AstList childrenList = handleModuleModel.getChildrenList();
        // 创建模块的上下文对象
        FeContext modelFeContext = createNewFeContext(feContext, handleModuleModel, inScopeValUnit);
        FlowStrand modelFlowStrand = new FlowStrand(atomTranParam, FlowStrand.SUB_PROCESS, modelFeContext);
        modelFlowStrand.setParentStep(curStep);
        // 初始化、执行处理模块
        AtomResult res = execList(modelFlowStrand, childrenList);
        // 组装处理模块结果
        ScopeValUnit scopeValUnit = PbCommonAtomUtils.createPutCtxScopeValUnit(feContext,flowUnit);
        setModuleResParam(modelFeContext, scopeValUnit, res);
        atomResult.copyValue(res);

        if (modelFlowStrand.isExit()) {
            flowStrand.setExit(true);
        }

        modelFlowStrand.setRetType(atomResult.getRetType());

        LOGGER.info("##Step[{}] HandleModule[{}]End!UseTime[{}]ms", curStep, atomTranParam, Timer.getUsedTime(startTime));
    }

    /**
     * 重新创建上下文对象并返回
     *
     * @param handleModuleModel 处理模块模型
     * @return FeContext 上下文对象
     */
    private FeContext createNewFeContext(FeContext feContext, HandleModuleModel handleModuleModel,
            ScopeValUnit scopeValUnit) {
        TranCode modelTranCode = handleModuleModel.getTranCode();
        // 参数赋值
        FeContext modelFeContext = new FeContext(feContext.getSysScope(), feContext.getTranScope(), feContext.getInHeaderScope(), scopeValUnit, feContext.getCacheClientApi());
        modelFeContext.setFeTranCode(feContext.getFeTranCode());
        modelFeContext.setBizType(modelTranCode.getBusiType());
        modelFeContext.setTranCode(modelTranCode.getTranCode());
        modelFeContext.setTranCodeObj(modelTranCode);
        modelFeContext.setSagaTransitionManager(feContext.getSagaTransitionManager());

        return modelFeContext;
    }

    /**
     * 获取处理模块的执行结果，放入流程上下文中
     *
     * @param modelFeContext 处理模块上下文对象
     * @param res            原子交易结果
     */
    private void setModuleResParam(FeContext modelFeContext,
            ScopeValUnit scopeValUnit, AtomResult res) {
        PbScope<Object> resPbScope = modelFeContext.getOutScope();
        scopeValUnit.put(RESULT_KEY, res.resToPbScope());
        if (Objects.nonNull(resPbScope) && !resPbScope.isEmpty()) {
            scopeValUnit.putAll(resPbScope);
        }
    }

    private void checkReqJsonSchema(FeContext feContext, HandleModuleModel handleModuleModel,
            ScopeValUnit scopeValUnit) {
        String allJsonSchemaFlag = feContext.getCacheClientApi().getSysCfg(JSON_SCHEMA_FLAG);
        if (!Objects.equals(JSON_SCHEMA_FLAG_TRUE, allJsonSchemaFlag)) {
            return;
        }
        TranCode modelTranCode = handleModuleModel.getTranCode();
        String apiName = modelTranCode.getBusiType() + "_" + modelTranCode.getApiCode() + "_" + REQ_JSON_SCHEMA_TYPE;
        Api api = feContext.getCacheClientApi().getApi(apiName);
        if (ObjectUtils.isEmpty(api)) {
            LOGGER.warn("[{}]-[{}],API接口配置为空!", modelTranCode.getBusiType(), modelTranCode.getApiCode());
            return;
        } else {
            try {
                ValidJsonUtils.validJsonSchema(scopeValUnit, api.getJsInfo(), modelTranCode.getIsBlocked());
            } catch (Exception e) {
                throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), String.format("[%s]-[%s],API接口配置校验异常", modelTranCode.getBusiType(), modelTranCode.getApiCode()), e);
            }
        }
        return;
    }
}

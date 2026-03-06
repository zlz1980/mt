package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.utils.StrUtils;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;

/**
 * @author liulei
 */
public class TempValBaseParse extends BaseParseStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempValBaseParse.class);
    /**
     * 临时变量赋值操作,只能赋值到tmp中
     * ${info.id}|${val.id}.toUpperCase()
     * ${info.name}|'aaa'.toUpperCase()
     * *${info.name}|W001
     * @param model 模型
     * @return Result
     * @throws FlowException 流程异常
     */
    @Override
    public AtomResult doHandle(FlowStrand flowStrand,AstModel model) {
        AtomResult atomResult = new AtomResult();
        FlowUnit unit = model.getUnit();
        normalHandle(flowStrand,unit);
        return atomResult;

    }

    private void normalHandle(FlowStrand flowStrand,FlowUnit unit) {
        Long startTime = Timer.getStartTime();
        String curStep = flowStrand.getCurStep();
        LOGGER.info("##Step[{}] Execute PbTmpValImpl[{}]Begin##",curStep,
        		unit.getAtomTranParam());
        LOGGER.info("AtomTran note[{}]",unit.getNote());
        FeContext feContext = flowStrand.getFeContext();
        PbScope<Object> tmpScope = feContext.getTmpScope();
        ScopeValUnit scopeValUnit = new ScopeValUnit();
        setTempValueList(feContext, scopeValUnit, unit);
        tmpScope.putAll(scopeValUnit);
        LOGGER.info("##Step[{}] PbTmpValImpl[{}]End!UseTime[{}]ms",curStep,
        		unit.getAtomTranParam(),Timer.getUsedTime(startTime));
    }

    private void setTempValueList(FeContext feContext,
                                  ScopeValUnit scopeValUnit , FlowUnit unit){
        if(CollectionUtils.isEmpty(unit.getParamList())){
            LOGGER.warn("flowParaList is null");
            return;
        }
        for(String param : unit.getParamList()){
            if(StringUtils.hasText(param)) {
                setTempValue(param, feContext,scopeValUnit);
            }
        }
        LOGGER.debug("scopeValUnit value [{}]",scopeValUnit);
    }

    private void setTempValue(String paramStr,FeContext feContext,ScopeValUnit scopeValUnit){
        String[] params = StringUtils.delimitedListToStringArray(paramStr, PARAM_SPILT_FLAG);
        if(params.length != PARAM_NUM_TWO){
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(),
                    "The number of parameters must be 2");
        }
        // 获取需要数据值
        String valKey = StrUtils.trim(params[1]);

        String key = MapUtils.spiltVal(StrUtils.trim(params[0]));

        FlowParaUtils.setFlowParaValue(feContext,scopeValUnit,key,valKey);
    }

}

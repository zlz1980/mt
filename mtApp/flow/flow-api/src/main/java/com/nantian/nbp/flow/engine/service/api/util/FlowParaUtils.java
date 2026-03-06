package com.nantian.nbp.flow.engine.service.api.util;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.ev.PbVal;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.DeepCloneUtils;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.cache.server.api.Constants.FLOW_ASYNC_MODULE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_MODULE;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @author w07362
 */
public class FlowParaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowParaUtils.class);

    private static final String THIS = "_this";

    public static void setFlowParaValues(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        setFlowParaValues(tranContext, scopeValUnit, flowUnit, Boolean.TRUE);
    }

    public static void setFlowParaValues(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit,
                                         boolean existsStoreMode) {
        String param = flowUnit.getAtomTranParam();
        boolean isSingleParam = !INIT_RET_VAL.equals(param)
                && !Objects.equals(FLOW_MODULE, flowUnit.getAtomTranCode())
                && !Objects.equals(FLOW_ASYNC_MODULE, flowUnit.getAtomTranCode())
                && CollectionUtils.isEmpty(flowUnit.getParamList());
        if (isSingleParam) {
            setFlowParaValByValid(tranContext, scopeValUnit, param, existsStoreMode);
            return;
        }
        List<String> paras = flowUnit.getParamList();
        if (!CollectionUtils.isEmpty(paras)) {
            for (String para : paras) {
                setFlowParaValByValid(tranContext, scopeValUnit, para, existsStoreMode);
            }
            return;
        }
        LOGGER.warn("Param and ParamList all is empty");
    }

    public static void setFlowParaValByValid(TranContext tranContext, ScopeValUnit scopeValUnit, String para,
                                             boolean existsStoreMode) {
        String[] params = StringUtils.delimitedListToStringArray(para, PARAM_SPILT_FLAG);
        if (params.length != PARAM_NUM_TWO) {
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("增强处理参数赋值格式非法，要求以|分割,请检查参数配置[{}]", para), null);
        }
        setFlowParaValue(tranContext, scopeValUnit, StrUtils.trim(params[0]), StrUtils.trim(params[1]), existsStoreMode);
    }

    public static void setFlowParaValue(TranContext tranContext, ScopeValUnit scopeValUnit, String keyName,
                                        String value) {
        setFlowParaValue(tranContext, scopeValUnit, keyName, value, Boolean.FALSE);
    }

    /**
     * 设置参数值的方法
     *
     * @param scopeValUnit    单元范围值对象，用于操作作用域中的键值对
     * @param keyName         键名，标识要设置或修改的变量或参数名称
     * @param valueExp        值，待设置或修改的变量或参数的新值
     * @param existsStoreMode 针对空值是否存储的标志
     */
    public static void setFlowParaValue(TranContext tranContext, ScopeValUnit scopeValUnit, String keyName,
                                        String valueExp, boolean existsStoreMode) {
        PbVal pbVal = tranContext.getCtxVal(valueExp);
        Object val = pbVal.getVal();
        String key = MapUtils.spiltVal(keyName);
        // 基础和字符串类型不做深拷贝
        if (!(val instanceof String) && !(val instanceof Number)) {
            val = DeepCloneUtils.deepClone(val);
        }
        if (Objects.equals(THIS, keyName)) {
            if (Objects.isNull(val)) {
                LOGGER.warn("当前向_this赋值的操作无效，keyName [{}]对应值为NULL", keyName);
                return;
            }

            if (!(val instanceof Map)) {
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), String.format("keyName [%s] 对应数据类型必须为Map", keyName), null);
            }
            scopeValUnit.putAll((Map<String, ?>) val);
            return;
        }

        if (existsStoreMode) {
            if (ObjectUtils.isEmpty(val)) {
                LOGGER.debug("KVSet keyName[{}],valueExp[{}],value[{}] isEmpty, 空值不写入当前作用域", keyName, valueExp, val);
            } else {
                LOGGER.debug("existsStoreMode KVSet keyName[{}],valueExp[{}],value[{}]", keyName, valueExp, val);
                MapUtils.setVal(scopeValUnit, key, val);
            }
        } else {
            if (ObjectUtils.isEmpty(val)) {
                LOGGER.debug("KVSet keyName[{}],valueExp[{}],value[{}] isEmpty, 空值写入当前作用域", keyName, valueExp, val);
            } else {
                LOGGER.debug("KVSet keyName[{}],valueExp[{}],value[{}]", keyName, valueExp, val);
            }
            MapUtils.setVal(scopeValUnit, key, val);
        }
    }

}

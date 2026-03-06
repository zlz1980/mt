package com.n.fbsp.atom.platform.seed.val;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_DOT_SPILT;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_ONE;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_THREE;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.RESULT_COUNT;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 获取数组中指定条件下的内容
 *
 * @author Lianghaizhen
 */
@Atom("base.FindFirstItemByList")
public class FindFirstItemByList implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindFirstItemByList.class);

    /**
     * 获取数组中指定条件下的内容
     * 输入：数组
     * 标签名称:XXX,不支持多层级嵌套xxx.xxxx.xxx
     * 标签对应的value:单引号:字符串,不带单引号:数字
     * <p>
     * 数组|标签名称|标签对应的value
     * 输出：匹配成功后同一维度下的所有标签
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象
     * @param flowUnit     当前单元
     * @return AtomResult
     *
     * @throws FlowException 流程异常
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || Objects.equals(INIT_RET_VAL, flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数按|分隔符拆分成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 如果参数数量小于3个，则抛出参数数量不正确的异常
        if (atomTranParams.length != PARAM_NUM_THREE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam));
        }
        // 获取从数组对象中需要对比的key
        String key = StrUtils.trim(atomTranParams[1]);
        // 将参数按.分隔符拆分成数组
        String[] keys = StringUtils.delimitedListToStringArray(key, PARAM_DOT_SPILT);
        // 如果参数数量不为1，则抛出参数数量不正确的异常
        if (keys.length != PARAM_NUM_ONE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数不支持多层级,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数不支持多层级,请检查参数配置[{}]", atomTranParams[1]));
        }
        Object val = tranContext.getCtxVal(atomTranParams[2]).getVal();
        // 获取数组对象
        Object objectVal = tranContext.getCtxVal(atomTranParams[0]).getVal();
        if (!ObjectUtils.isEmpty(objectVal)) {
            if (!(objectVal instanceof List)) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数非数组对象,请检查参数配置[{}]", atomTranParams[0]);
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数非数组对象,请检查参数配置[{}]", atomTranParams[0]));
            }
            List<Map<String, Object>> list = (List<Map<String, Object>>) objectVal;
            boolean isSuccess = false;
            // 遍历数组，根据数组对象的key,获取数组对象的value与传入的value对比
            for (Map<String, Object> map : list) {
                if (Objects.equals(map.get(key), val)) {
                    // 如果匹配成功，则将匹配到的数组写入到当前作用域scopeValUnit
                    scopeValUnit.putAll(map);
                    isSuccess = true;
                    break;
                }
            }
            int count = isSuccess ? 1 : 0;
            LOGGER.info("数组元素匹配完成,size[{}]", count);
            scopeValUnit.put(RESULT_COUNT, count);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

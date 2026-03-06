package com.n.fbsp.atom.platform.seed.val;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.ev.PbVal;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.RESULT_COUNT;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 决策表操作,根据原子交易参数最后一个数据项去匹配所有的增强处理的最后一个数据项,
 * 要求所有增强处理的最后一个匹配项保持唯一，当原子交易参数与增强处理参数匹配成功后，
 * 匹配到的这条增强处理数据后，按照原子交易参数配置的目标转换参数key进行数据映射并
 * 写入当前原子交易的作用域。size为1。
 * 决策表操作在应用首次启动时会解析出增强处理中的全量参数并写入缓存，缓存键名为
 * [DECISIONTBL_+业务类型+交易码+作用域名称]，流程中匹配将直接从缓存中进行hash
 * 查找以提高匹配效率。如果决策表存在默认匹配值：则需要在增强处理中配置一条数据项为
 * default的数据，default数据匹配优先级最低，当所有其他增强处理都未匹配到时，会默认以default
 * 数据进行返回，如果未配置default数据，且决策表未匹配到数据，则匹配结果会置为空，size为0。
 *
 * @Author : Liang Haizhen
 * @Date 2024/9/25 13:35
 **/
@Atom("base.DecisionTable")
public class DecisionTableImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecisionTableImpl.class);
    private static final String DEFAULT = "default";
    @Autowired
    private CacheClientApi cacheClientApi;

    /**
     * 1.原子交易参数（以 | 分割）：
     * 匹配结果Key1 | 匹配结果Key2 | 匹配结果Key3 |...... | 匹配结果Keyn | 匹配值
     * 2.增强处理参数（以 | 分割）：
     * 匹配结果Val1 | 匹配结果Val2 | 匹配结果Val3 |...... | 匹配结果Valn |被匹配值
     * <p>
     * 应用举例：
     * 原子交易参数设置：
     * respCd|respDesc|subRespCd|subRespDesc|${mapperVal}
     * <p>
     * 增强处理参数设置：
     * 'value11'|'value12'|'value13'|'value14'|'mapperVal1'
     * 'value21'|'value22'|'value23'|'value24'|'mapperVal2'
     * 'value31'|'value32'|'value33'|'value34'|'mapperVal3'
     * 'value41'|'value42'|'value43'|'value44'|'mapperVal4'
     *
     * @param tranContext
     * @param scopeValUnit 作用域值单元
     * @param flowUnit     流程单元
     * @return 原子操作结果对象
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 创建原子操作结果对象
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || Objects.equals(INIT_RET_VAL, flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        // 获取流程单元中的原子交易参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数按|分隔符拆分成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 如果参数数量小于2个，则抛出参数数量不正确的异常
        if (atomTranParams.length < PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数小于2,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数小于2,请检查参数配置[{}]", atomTranParam));
        }
        String caCheId = flowUnit.getBusiType() + "_" + flowUnit.getTranCode() + "_" + flowUnit.getAscope();
        // 初始化哈希映射对象用于存储决策表数据
        Map<String, Object> mapperMap;
        // 从缓存获取决策表数据
        mapperMap = cacheClientApi.getDecisionTable(caCheId);
        // 如果决策表数据为空，则根据业务类型和交易代码从增强处理中获取决策表数据
        if (CollectionUtils.isEmpty(mapperMap)) {
            LOGGER.error("未获取到决策表的缓存信息,请检查决策表配置[{}]", caCheId);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到决策表的缓存信息,请检查决策表配置[{}]", caCheId));
        }
        String decisionValue = atomTranParams[atomTranParams.length - 1];
        PbVal pbVal = tranContext.getCtxVal(decisionValue);
        if (!pbVal.isExists()) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "待匹配参数值为空,请检查参数配置[{}]", decisionValue);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("待匹配参数值为空,请检查参数配置[{}]", decisionValue));
        }
        // 根据最后一个参数的值从决策表中获取对应的映射对象
        String key = (String) pbVal.getVal();
        Map<String, Object> result;
        int count;
        Object matchValue = mapperMap.get(key);
        if (!ObjectUtils.isEmpty(matchValue)) {
            result = paramToMap((String) matchValue, atomTranParams, tranContext);
            count = 1;
        } else {
            // 如果配置了默认结果，则写入当前作用域
            Object defaultValue = mapperMap.get(DEFAULT);
            if (!ObjectUtils.isEmpty(defaultValue)) {
                result = paramToMap((String) defaultValue, atomTranParams, tranContext);
                count = 1;
            } else {
                LOGGER.warn("增强处理未匹配到结果，输入待匹配值[{}]", key);
                result = new HashMap<>();
                count = 0;
            }
        }
        LOGGER.info("决策表操作匹配完成,size[{}]", count);
        scopeValUnit.put(RESULT_COUNT, count);
        // 匹配结果写入当前作用域
        result.forEach((k, v) -> MapUtils.setVal(scopeValUnit, k, v));
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        // 设置操作结果为成功并返回原子操作结果对象
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

    /**
     * 将参数转换为键值对映射
     * 此方法的主要目的是处理原子参数，将其映射到一个可操作的对象中
     * 它首先通过参数分隔符将原子参数字符串分割成字符串数组，然后根据原子交易参数数组的长度检查参数是否匹配
     * 如果参数不匹配，将记录错误并抛出异常如果匹配，则将以键值对的形式将参数存储在一个HashMap中
     *
     * @param matchResult    匹配到的结果
     * @param atomTranParams 原子交易参数数组，表示期望的参数结构
     * @return 包含所有参数键值对的映射如果atomParam不符合期望的格式，可能返回空或包含不完整数据的Map
     *
     * @throws FlowException 如果atomParam的参数个数与原子交易参数个数不匹配，则抛出此异常
     */
    private Map<String, Object> paramToMap(String matchResult, String[] atomTranParams, TranContext tranContext) {
        // 将字符串按照参数分隔符分割成字符串数组
        String[] params = StringUtils.delimitedListToStringArray(matchResult, PARAM_SPILT_FLAG);
        // 检查参数数组长度是否符合预期
        if (params.length != atomTranParams.length) {
            // 参数长度不符合预期，记录错误日志并抛出异常
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数个数与原子交易参数个数不匹配,请检查参数配置[{}]", matchResult);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数个数与原子交易参数个数不匹配,请检查参数配置[{}]", matchResult));
        }
        // 创建一个临时的HashMap，用于存储键值对结果
        Map<String, Object> mapperMap = new HashMap<>();
        // 对每个参数进行处理，如果符合某个模式则进行替换
        for (int i = 0; i < atomTranParams.length - 1; i++) {
            // 将处理后的参数添加到临时HashMap中
            mapperMap.put(StrUtils.trim(atomTranParams[i]), tranContext.getCtxVal(StrUtils.trim(params[i])).getVal());
        }
        // 返回最终的键值对结果HashMap
        return mapperMap;
    }

}

package com.n.fbsp.atom.platform.seed.val;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.cache.server.api.CacheClientApi;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.utils.Constants.ATOM_RESULT_ATTR_SIZE;
import static com.nantian.nbp.utils.Constants.STATIC_DATA_KEY_DELIMITER;

/**
 * 静态表缓存数据获取
 *
 * @Author : Liang Haizhen
 * @Date 2024/8/29 11:19
 **/
@Atom("base.StaticTableCaCheRead")
public class StaticTableCaCheReadImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticTableCaCheReadImpl.class);

    @Autowired
    private CacheClientApi cacheClientApi;

    /**
     * 静态表缓存数据获取
     * 原子交易配置参数：
     * cacheid对应t_pb_statictbl_app中的cacheid
     * 主键对应t_pb_statictbl_app中的keyname中的配置
     * <p>
     * cacheid|主键1|主键2|主键N|...
     * t_eb_param_det|${pk1}|${pk2}|${pkn}
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象
     * @param flowUnit     当前单元
     * @throws FlowException 流程异常
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象并设置默认返回类型为成功
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || Objects.equals(INIT_RET_VAL, flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        // 获取原子交易配置参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 处理原子交易参数字符串，将其分割为参数数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数数量校验，确保至少有两个参数（cacheid 和至少一个主键）
        if (atomTranParams.length < PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数小于2,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数小于2,请检查参数配置[{}]", atomTranParam));
        }
        String tableKey = (String) tranContext.getValue(StrUtils.trim(atomTranParams[0]));
        // 拼接静态表缓存keyName值
        StringJoiner cacheKey = new StringJoiner(STATIC_DATA_KEY_DELIMITER);
        for (int i = 1; i < atomTranParams.length; i++) {
            cacheKey.add(StrUtils.toStrDefBlank(tranContext.getCtxVal(StrUtils.trim(atomTranParams[i])).getVal()));
        }
        // 获取缓存信息
        Map<String, Object> tableResult;
        tableResult = cacheClientApi.getStaticTable(tableKey, cacheKey.toString());
        int count = 0;
        // 如果缓存信息为空，则记录日志
        if (ObjectUtils.isEmpty(tableResult)) {
            LOGGER.warn("未获取到静态表的缓存信息 [{}]", atomTranParams[0]);
        } else {
            // 更新计数器，表示成功获取到数据，并将结果写入当前作用域
            count = 1;
            // 数据库查询结果写入当前作用域
            scopeValUnit.putAll(tableResult);
        }
        LOGGER.info("静态表缓存信息获取完成,size[{}]", count);
        scopeValUnit.put(ATOM_RESULT_ATTR_SIZE, count);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        // 返回处理结果
        return atomResult;
    }
}

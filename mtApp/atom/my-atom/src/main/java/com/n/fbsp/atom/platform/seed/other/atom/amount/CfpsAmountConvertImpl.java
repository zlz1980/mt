package com.n.fbsp.atom.platform.seed.other.atom.amount;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.n.fbsp.atom.platform.seed.other.utils.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 贷记卡核心金额转换为请求渠道预期格式
 * 1.原子交易参数：
 * 目标转换结果key前缀|待转换金额取值路径
 * <p>
 * 举例1：transAt|${in.basInf.transAt}
 * 转换结果：
 * y_transAt（借记卡）
 * c_transAt（贷记卡）
 * <p>
 * 2.增强处理参数（以 | 分割）：
 * 无需配置
 * 3.错误码及错误描述：
 * 无需配置
 *
 * @Author :
 */
@Atom("fbsp.CfpsAmountConvert")
public class CfpsAmountConvertImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CfpsAmountConvertImpl.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        List<String> paramList = flowUnit.getParamList();
        if (CollectionUtils.isEmpty(paramList)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数未配置");
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数未配置"));
        }
        // 日志输出增强处理参数信息
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("增强处理参数 [{}]", JsonUtil.objToString(paramList));
        }
        for (String paramStr : paramList) {
            // 将参数字符串按分隔符分割成数组
            String[] params = StringUtils.delimitedListToStringArray(paramStr, PARAM_SPILT_FLAG);
            if (params.length != PARAM_NUM_TWO) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + " 增强处理参数个数不为2,请检查参数配置[{}]", paramStr);
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数个数不为2,请检查参数配置[{}]", paramStr));
            }
            processAmount(tranContext, scopeValUnit, params);
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

    private void processAmount(TranContext tranContext, ScopeValUnit scopeValUnit, String[] atomTranParams) {
        // 解析参数：目标键值后缀和待解析金额栏位
        String key = MapUtils.spiltVal(StrUtils.trim(atomTranParams[0]));
        //fbsp系统内金额key
        String yKey = FBSP_AMOUNT_PRE + key;
        //银联金额key
        String cupsKey = UPS_AMOUNT_PRE + key;
        //连通传统金额key
        String ecsKey = YT_AMOUNT_PRE + key;
        //连通无卡金额key
        String ecswkKey = YTWK_AMOUNT_PRE + key;
        Object objectVal = tranContext.getCtxVal(atomTranParams[1]).getVal();
        if (ObjectUtils.isEmpty(objectVal)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "待转换金额为空,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("待转换金额为空,请检查参数配置[{}]", atomTranParams[1]));
        }
        // 贷记卡核心返回金额，12位前补0以分为单位
        String cfpsAmount = objectVal.toString();
        //对cfpsAmount进行正则表达式数据校验，仅支持数字
        if (!cfpsAmount.matches("^[0-9]+$")) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[1], cfpsAmount);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[1], cfpsAmount));
        }
        try {
            BigDecimal val = new BigDecimal(cfpsAmount);
            BigDecimal yAmount = val.multiply(new BigDecimal("0.01"));
            //采用toPlainString方法输出字符串，避免科学计数法影响
            String cupsAmount = val.toPlainString();
            String ecswkAmount = yAmount.toPlainString();
            //fbsp内部金额元为单位
            MapUtils.setVal(scopeValUnit, yKey, yAmount);
            //新银联 json变长
            MapUtils.setVal(scopeValUnit, cupsKey, cupsAmount);
            //运通传统8583
            MapUtils.setVal(scopeValUnit, ecsKey, cfpsAmount);
            //运通无卡快捷
            MapUtils.setVal(scopeValUnit, ecswkKey, ecswkAmount);
            LOGGER.info("贷记卡核心金额转换完成,原金额[{}],银联渠道金额[{}]:[{}],运通传统渠道金额[{}]:[{}],运通无卡渠道金额[{}]:[{}]", cfpsAmount, cupsKey, cupsAmount, ecsKey, cfpsAmount, ecswkKey, ecswkAmount);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "贷记卡核心金额转换异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("贷记卡核心金额转换异常,异常信息[{}]", e.getMessage()), e);
        }
    }
}

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
import java.text.DecimalFormat;
import java.util.List;


import static com.n.fbsp.atom.platform.seed.other.utils.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 借记卡核心金额转换为请求渠道预期格式
 * 1.原子交易参数：
 * 无需配置
 * <p>
 * 2.增强处理参数（以 | 分割）：
 * 目标转换结果key前缀|待转换金额取值路径
 * <p>
 * 举例1：transAt|${in.basInf.transAt}
 * 转换结果：
 * y_transAt
 * cups_transAt
 * ecs_transAt
 * ecswk_transAt
 * <p>
 * 3.错误码及错误描述：
 * 无需配置
 *
 * @Author :
 */
@Atom("fbsp.NcbsAmountConvert")
public class NcbsAmountConvertImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NcbsAmountConvertImpl.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        List<String> paramList = flowUnit.getParamList();
        if (CollectionUtils.isEmpty(paramList)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数未配置");
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("增强处理参数未配置"), null);
        }
        // 日志输出增强处理参数信息
        LOGGER.debug("增强处理参数 [{}]", JsonUtil.objToString(paramList));
        for (String paramStr : paramList) {
            // 将参数字符串按分隔符分割成数组
            String[] params = StringUtils.delimitedListToStringArray(paramStr, PARAM_SPILT_FLAG);
            if (params.length != PARAM_NUM_TWO) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数个数不为2,请检查参数配置[{}]", paramStr);
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("增强处理参数个数不为2,请检查参数配置[{}]", paramStr), null);
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
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("待转换金额为空,请检查参数配置[{}]", atomTranParams[1]), null);
        }
        // 借记卡核心金额 两位小数  double  单位 元
        String ncbsAmount = objectVal.toString();
        //对transAt进行正则表达式数据校验，仅支持数字和小数点
        if (!ncbsAmount.matches("^[0-9.]+$")) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[1], ncbsAmount);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[1], ncbsAmount), null);
        }
        try {
            BigDecimal val = new BigDecimal(ncbsAmount);
            // 格式化输出，确保小数部分有两位
            DecimalFormat df = new DecimalFormat("#.00");
            // 将格式化后的字符串转换回BigDecimal
            BigDecimal yAmount = new BigDecimal(df.format(val));
            //银联金额分为单位 string
            String cupsAmount = val.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
            String ecsAmount = StrUtils.padLeftWithZero(12, cupsAmount);
            String ecswkAmount = yAmount.toString();

            MapUtils.setVal(scopeValUnit, yKey, yAmount);
            MapUtils.setVal(scopeValUnit, cupsKey, cupsAmount);
            MapUtils.setVal(scopeValUnit, ecsKey, ecsAmount);
            MapUtils.setVal(scopeValUnit, ecswkKey, ecswkAmount);
            LOGGER.info("借记卡核心金额转换完成,原金额[{}],银联渠道金额[{}]:[{}],运通传统渠道金额[{}]:[{}],运通无卡渠道金额[{}]:[{}]", ncbsAmount, cupsKey, cupsAmount, ecsKey, ecsAmount, ecswkKey, ecswkAmount);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "借记卡核心金额转换异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("借记卡核心金额转换异常,异常信息[{}]", e.getMessage()), e);
        }
    }
}

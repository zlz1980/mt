package com.n.fbsp.atom.platform.seed.other.atom.amount;


import com.n.fbsp.atom.platform.seed.other.utils.AmountUtils;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.n.fbsp.atom.platform.seed.other.utils.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 运通无卡快捷金额转换
 * 1.原子交易参数：
 * 待转换金额取值路径
 * <p>
 * 举例1：${in.basInf.transAt}
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
@Atom("fbsp.EcswkAmountConvert")
public class EcswkAmountConvertImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EcswkAmountConvertImpl.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程参数字符串
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数字符串按分隔符分割成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数校验：判断参数个数是否正确
        if (atomTranParams.length != PARAM_NUM_ONE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam), null);
        }
        // 解析参数：目标键值后缀
        String amountKey = AmountUtils.getAmountKey(atomTranParams[0]);
        String yKey = FBSP_AMOUNT_PRE + amountKey;
        String cKey = DJK_AMOUNT_PRE + amountKey;
        Object objectVal = tranContext.getCtxVal(atomTranParams[0]).getVal();
        if (ObjectUtils.isEmpty(objectVal)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "待转换金额为空,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("待转换金额为空,请检查参数配置[{}]", atomTranParams[0]), null);
        }
        String trxAmt = objectVal.toString();
        //对trxAmt进行正则表达式数据校验，强制要求前3位为字母（货币符号）整数部分限制为1-16位数字（\\d{1,16}）强制要求小数点后两位（\\.\\d{2}）
        if (!trxAmt.matches("^[A-Za-z]{3}\\d{1,16}\\.\\d{2}$")) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[0], trxAmt);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[0], trxAmt), null);
        }

        try {
            String valStr = trxAmt.substring(3);
            BigDecimal yTrxAmt = new BigDecimal(valStr);
            // 格式化输出，确保小数部分有两位
            DecimalFormat df = new DecimalFormat("#.00");
            String formatted = df.format(yTrxAmt);
            // 将格式化后的字符串转换回BigDecimal
            yTrxAmt = new BigDecimal(formatted);
            String cTrxAmt = yTrxAmt.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
            String cTrxAmt1 = StrUtils.padLeftWithZero(12, cTrxAmt);
            MapUtils.setVal(scopeValUnit, yKey, yTrxAmt);
            MapUtils.setVal(scopeValUnit, cKey, cTrxAmt1);
            LOGGER.info("连通无卡快捷渠道金额转换完成,原金额[{}],借记卡核心金额[{}]:[{}],贷记卡核心金额[{}]:[{}]", trxAmt, yKey, yTrxAmt, cKey, cTrxAmt1);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "连通无卡快捷渠道金额转换异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("连通无卡快捷渠道金额转换异常,异常信息[{}]", e.getMessage()), e);
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

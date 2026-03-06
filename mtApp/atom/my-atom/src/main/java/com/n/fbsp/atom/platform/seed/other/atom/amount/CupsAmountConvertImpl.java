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
 * 银联金额转换
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
@Atom("fbsp.CupsAmountConvert")
public class CupsAmountConvertImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CupsAmountConvertImpl.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        // 获取流程参数字符串
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数字符串按分隔符分割成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数校验：判断参数个数是否正确
        if (atomTranParams.length != PARAM_NUM_ONE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam));
        }
        // 解析参数：目标键值后缀
        String amountKey = AmountUtils.getAmountKey(atomTranParams[0]);
        String yKey = FBSP_AMOUNT_PRE + amountKey;
        String cKey = DJK_AMOUNT_PRE + amountKey;
        Object objectVal = tranContext.getCtxVal(atomTranParams[0]).getVal();
        if (ObjectUtils.isEmpty(objectVal)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "待转换金额为空,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("待转换金额为空,请检查参数配置[{}]", atomTranParams[0]));
        }

        // 银联basInf.transAt
        String transAt = objectVal.toString();
        //对transAt进行正则表达式数据校验，仅支持数字
        if (!transAt.matches("^[0-9]+$")) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[0], transAt);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("金额栏位[{}]对应的金额值[{}]格式错误,请检查参数", atomTranParams[0], transAt));
        }
        try {
            BigDecimal val = new BigDecimal(transAt);
            BigDecimal yTransAt = val.multiply(new BigDecimal("0.01"));
            // 格式化输出，确保小数部分有两位
            DecimalFormat df = new DecimalFormat("#.00");
            String formatted = df.format(yTransAt);
            // 将格式化后的字符串转换回BigDecimal
            yTransAt = new BigDecimal(formatted);
            String cTransAt = StrUtils.padLeftWithZero(12, transAt);
            MapUtils.setVal(scopeValUnit, yKey, yTransAt);
            MapUtils.setVal(scopeValUnit, cKey, cTransAt);
            LOGGER.info("银联渠道金额转换完成,原金额[{}],借记卡核心金额[{}]:[{}],贷记卡核心金额[{}]:[{}]", transAt, yKey, yTransAt, cKey, cTransAt);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "银联渠道金额转换异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("银联渠道金额转换异常,异常信息[{}]", e.getMessage()), e);
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

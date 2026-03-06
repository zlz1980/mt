package com.n.fbsp.atom.platform.seed.other.atom.amount;


import com.n.fbsp.atom.platform.seed.other.utils.Constants;
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

import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 原子交易功能：8583报文账户余额金额转换+原子参数输入说明：入参：账户类型|账面余额|当日可用余额，出参：amount（8583报文中账户余额的金额值）
 举例：
 01|${in.basInf.transAt}|${in.basInf.transAt2}+增强处理输入说明：无需配置+错误码输入说明：无需配置
 *
 * @Author :
 */
@Atom("fbsp.AcctBalAmountConvert8583")
public class AcctBalAmountConvert8583Impl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcctBalAmountConvert8583Impl.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        try {
            if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "8583余额转换原子交易：参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("8583余额转换原子交易：参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
            }
            // 获取流程参数字符串
            String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
            // 将参数字符串按分隔符分割成数组
            String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
            // 参数校验：判断参数个数是否正确
            if (atomTranParams.length != PARAM_NUM_THREE) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "8583余额转换原子交易：参数个数不为{},请检查参数配置[{}]", PARAM_NUM_THREE,atomTranParam);
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("8583余额转换原子交易：参数个数不为{},请检查参数配置[{}]", PARAM_NUM_THREE,atomTranParam));
            }

            //从上下文获取账面余额
            Object ledgerBalanceAmountObj = tranContext.getCtxVal(atomTranParams[1]).getVal();
            if (ledgerBalanceAmountObj==null || !StringUtils.hasText(ledgerBalanceAmountObj.toString())) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "8583余额转换原子交易：账面余额未能在上下文获取到,上下文中的KEY[{}]", atomTranParams[1]);
                ledgerBalanceAmountObj = "0";
            }
            //从上下文获取当日可用余额
            Object availableBalanceAmountObj = tranContext.getCtxVal(atomTranParams[2]).getVal();
            if (availableBalanceAmountObj==null || !StringUtils.hasText(availableBalanceAmountObj.toString())) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "8583余额转换原子交易：当日可用余额未能在上下文获取到,上下文中的KEY[{}]", atomTranParams[2]);
                availableBalanceAmountObj = "0";
            }

            String ledgerBalanceAmount = ((String)ledgerBalanceAmountObj).trim();
            String availableBalanceAmount = ((String)availableBalanceAmountObj).trim();
            String transAt =    atomTranParams[0]+"01"+"156"+getAmountFlag(ledgerBalanceAmount) +     amountConvert(ledgerBalanceAmount);
            transAt = transAt+  atomTranParams[0]+"02"+"156"+getAmountFlag(availableBalanceAmount) +  amountConvert(availableBalanceAmount);

            MapUtils.setVal(scopeValUnit, Constants.AMOUNT_KEY, transAt);

            atomResult.setRetType(RetType.SUCCESS);
            return atomResult;
        }catch (Exception e){
            LOGGER.error("8583余额转换原子交易：参数[{}]执行失败", flowUnit.getAtomTranParam()+"",e);
            atomResult.setRetType(RetType.FAILED);
            atomResult.setErrMsg("8583余额转换原子交易执行失败");
            return atomResult;
        }
    }

    private String getAmountFlag(String amount){
        return (amount.startsWith("-")?"D":"C");
    }

    private String amountConvert(String amount){
        //金额处理：去掉负号
        if(amount.startsWith("-")){
            amount = amount.substring(1);
        }
        return StrUtils.padLeftWithZero(12,new BigDecimal(amount).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
    }

}

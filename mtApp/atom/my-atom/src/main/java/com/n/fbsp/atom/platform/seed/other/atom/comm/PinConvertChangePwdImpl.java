package com.n.fbsp.atom.platform.seed.other.atom.comm;


import com.n.fbsp.atom.platform.seed.other.utils.Constants;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.springframework.util.StringUtils;

import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 原子交易功能：PIN转换(行内->行内改密)
 * 原子参数输入说明：入参：原PIN密钥密文|新PIN密钥密文|目标系统服务名
 * 出参：pinKeyOld(PIN原密钥密文),pinKeyNew(PIN新密钥密文)
 * 举例：
 * ${in.data.oldPwdPinCode.pinKey}|${in.data.newPwdPinCode.pinKey}|'FBSP-SUBNOA-GatewayService'
 * 增强处理输入说明：
 * 无需配置
 * 错误码输入说明：
 * 无需配置
 *
 * @创建人 fbsp
 * @创建时间 2025/6/11 18:01
 * @描述 PIN转换：行内->行内，改密码场景
 */
@Atom("fbsp.PinConvertChangePwd")
public class PinConvertChangePwdImpl extends PinConvertBankToBank {

    /**
     * 重写doConvertPin方法，用于处理PIN转换的原子交易
     *
     * @param atomTranParam 原子交易参数，包含多个子参数通过特定分隔符连接的字符串
     * @param scopeValUnit  交易结果的存储单元
     * @param tranContext   交易上下文，用于获取交易过程中需要的参数
     * @throws Exception 如果处理过程中发生错误
     */
    @Override
    protected void doConvertPin(String atomTranParam, ScopeValUnit scopeValUnit,
            TranContext tranContext) throws Exception {
        //将参数字符串按分隔符分割成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        //参数校验：判断参数个数是否正确
        if (atomTranParams.length != PARAM_NUM_THREE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为{},请检查参数配置[{}]", PARAM_NUM_THREE, atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为{},请检查参数配置[{}]", PARAM_NUM_THREE, atomTranParam));
        }
        //从上下文获取原PIN密钥密文
        String pinKeyCipherOld = tranContext.getValue(atomTranParams[0]).toString();
        if (!StringUtils.hasText(pinKeyCipherOld)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到原PIN密钥密文,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到原PIN密钥密文,请检查参数配置[{}]", atomTranParams[0]));
        }
        //从上下文获取新PIN密钥密文
        String pinKeyCipherNew = tranContext.getCtxVal(atomTranParams[1]).toString();
        if (!StringUtils.hasText(pinKeyCipherNew)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到新PIN密钥密文,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到新PIN密钥密文,请检查参数配置[{}]", atomTranParams[1]));
        }
        //参数校验：判断行内目标系统服务名是否为空
        String serviceName = tranContext.getValue(atomTranParams[2]).toString();
        //参数校验：判断行内目标系统服务名是否为空
        if (!StringUtils.hasText(serviceName)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到行内目标系统服务名,请检查参数配置[{}]", atomTranParams[2]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到行内目标系统服务名,请检查参数配置[{}]", atomTranParams[2]));
        }
        //参数校验：判断调用方HTTP头中的验签公钥名是否上送
        String edspMacKey = tranContext.getInHeaderScope().get(Constants.SC_ESP_MAC_KEY);
        if (!StringUtils.hasText(edspMacKey)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "调用方HTTP头中的验签公钥名为空[{}]", Constants.SC_ESP_MAC_KEY);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("调用方HTTP头中的验签公钥名为空[" + Constants.SC_ESP_MAC_KEY + "]"));
        }
        this.convertPinChangePwd(pinKeyCipherOld, pinKeyCipherNew, atomTranParams[2].trim(), edspMacKey, scopeValUnit);
    }


    /**
     * @param pinKeyCipherOld ：原PIN密钥密文
     * @param pinKeyCipherNew ：新PIN密钥密文
     * @param tgtServiceName  ：目标系统服务名
     * @param edspMacKey      ：源系统的验签公钥名
     * @param scopeValUnit    ：本原子交易上下文，
     */
    protected void convertPinChangePwd(String pinKeyCipherOld, String pinKeyCipherNew, String tgtServiceName,
            String edspMacKey, ScopeValUnit scopeValUnit) throws Exception {
        super.convertPinCebToCeb(pinKeyCipherOld, tgtServiceName, edspMacKey, scopeValUnit);
        //将返回值放到上下文，供后续流程使用，旧PIN密钥密文：pinKeyCipherOld，新PIN密钥密文：pinKeyCipherNew
        MapUtils.setVal(scopeValUnit, Constants.PIN_KEY_CIPHER + "Old", scopeValUnit.get(Constants.PIN_KEY_CIPHER));
        scopeValUnit.remove(Constants.PIN_KEY_CIPHER);
        super.convertPinCebToCeb(pinKeyCipherNew, tgtServiceName, edspMacKey, scopeValUnit);
        MapUtils.setVal(scopeValUnit, Constants.PIN_KEY_CIPHER + "New", scopeValUnit.get(Constants.PIN_KEY_CIPHER));
        scopeValUnit.remove(Constants.PIN_KEY_CIPHER);
    }

}

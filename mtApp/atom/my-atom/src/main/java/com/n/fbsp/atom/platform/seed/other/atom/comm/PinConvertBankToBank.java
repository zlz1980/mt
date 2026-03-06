package com.n.fbsp.atom.platform.seed.other.atom.comm;


import com.n.fbsp.atom.platform.seed.other.utils.Constants;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.springframework.util.StringUtils;


import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 *
 * 原子交易功能：PIN转换(行内->行内)
 * 原子参数输入说明：
 * 入参：PIN密钥密文|目标系统服务名，
 * 出参：pinKey(PIN密钥密文)
 * 举例：
 * ${in.data.pwdPinCode.pinKey}|'FBSP-SUBNOA-GatewayService'
 * 增强处理输入说明：无需配置
 * 错误码输入说明：无需配置
 *
 * @创建人 fbsp
 * @创建时间 2025/6/11 18:01
 * @描述 PIN转换：行内->行内
 */
@Atom("fbsp.PinConvertBankToBank")
public class PinConvertBankToBank extends AbstractPinConvertImpl {


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
        if (atomTranParams.length != PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为{},请检查参数配置[{}]", PARAM_NUM_TWO, atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为{},请检查参数配置[{}]", PARAM_NUM_TWO, atomTranParam));
        }
        //从上下文获取PIN密钥密文
        String pinKeyCipher = tranContext.getValue(atomTranParams[0]).toString();
        if (!StringUtils.hasText(pinKeyCipher)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到PIN密钥密文,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到PIN密钥密文,请检查参数配置[{}]", atomTranParams[0]));
        }
        //参数校验：判断行内目标系统服务名是否为空
        String serviceName = tranContext.getValue(atomTranParams[1]).toString();
        if (!StringUtils.hasText(serviceName)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到行内目标系统服务名,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到行内目标系统服务名,请检查参数配置[{}]", atomTranParams[1]));
        }
        //参数校验：判断调用方HTTP头中的验签公钥名是否上送
        String edspMacKey = tranContext.getInHeaderScope().get(Constants.SC_ESP_MAC_KEY);
        if (!StringUtils.hasText(edspMacKey)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "调用方HTTP头中的验签公钥名为空[{}]", Constants.SC_ESP_MAC_KEY);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("调用方HTTP头中的验签公钥名为空[{}]", Constants.SC_ESP_MAC_KEY));
        }
        this.convertPinCebToCeb(pinKeyCipher, serviceName, edspMacKey, scopeValUnit);
    }


    /**
     * @param pinKeyCipher   ：PIN密钥密文
     * @param tgtServiceName ：行内目标系统服务名
     * @param edspMacKey     ：源系统的验签公钥名
     * @param scopeValUnit   ：本原子交易上下文：pinKeyCipher：目的系统的PIN密钥密文
     * @throws Exception
     */
    protected void convertPinCebToCeb(String pinKeyCipher, String tgtServiceName, String edspMacKey,
            ScopeValUnit scopeValUnit) throws Exception {
        // 从缓存中获取加密平台所需的密钥名
//        String fbspPinVk = esscAssistant.getEsscKeyName(FBSP_PIN_VK);
//        String fbspMacPk = esscAssistant.getEsscKeyName(FBSP_MAC_PK);
//        LOGGER.info("调用加密平台参数：pinKeyCipher={}, edspMacKey={},fbspMacPk={},fbspPinVk={},tgtServiceName={}", pinKeyCipher, edspMacKey, fbspMacPk, fbspPinVk, tgtServiceName);
//        //调用加密平台转PIN，函数名:CebTransPINBetweenPK（加密随机PIN密钥的外层公钥之间转加密（320交易））
//        String szDstCipertextbyPK = getEsscAPI().CebTransPINBetweenPK(pinKeyCipher, edspMacKey, fbspMacPk, fbspPinVk, tgtServiceName);
//        //将返回值放到上下文，供后续流程使用
//        MapUtils.setVal(scopeValUnit, Constants.PIN_KEY_CIPHER, szDstCipertextbyPK);
    }


}

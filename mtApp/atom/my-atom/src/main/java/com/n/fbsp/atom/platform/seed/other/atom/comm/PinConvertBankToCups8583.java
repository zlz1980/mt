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
 * 原子交易功能：PIN转换(行内->8583规范)
 * 原子参数输入说明：
 * 入参：PIN密文|PIN密钥密文|请求系统账号|目标系统账号|卡组织秘钥配置名称
 * 出参：pinValue（PIN密文）
 * 举例：
 * ${in.data.pwdPinCode.pinValue}|${in.data.pwdPinCode.pinKey}|${in.data.srcAccNo}|${in.data.tgtAccNo}|'618_mcs_mac_key_name'
 * <p>
 * 增强处理输入说明：
 * 无需配置
 * <p>
 * 错误码输入说明：
 * 无需配置
 *
 * @创建人 fbsp
 * @创建时间 2025/6/11 18:01
 * @描述 PIN转换：行内->8583规范
 *
 */
@Atom("fbsp.PinConvertBankToCups8583")
public class PinConvertBankToCups8583 extends AbstractPinConvertImpl {


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
        if (atomTranParams.length != PARAM_NUM_FIVE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为{},请检查参数配置[{}]", PARAM_NUM_FIVE, atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("参数个数不为{},请检查参数配置[{}]", PARAM_NUM_FOUR, atomTranParam));
        }
        //从上下文获取PIN密文
        String pinCipher = tranContext.getValue(atomTranParams[0]).toString();
        if (!StringUtils.hasText(pinCipher)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到PIN密文,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到PIN密文,请检查参数配置[{}]", atomTranParams[0]));
        }
        //从上下文获取PIN密钥密文
        String pinKeyCipher = tranContext.getValue(atomTranParams[1]).toString();
        if (!StringUtils.hasText(pinKeyCipher)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到PIN密钥密文,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到PIN密钥密文,请检查参数配置[{}]", atomTranParams[1]));
        }
        //参数校验：判断请求系统账号是否为空
        String srcAccNo = tranContext.getValue(atomTranParams[2]).toString();
        if (!StringUtils.hasText(srcAccNo)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到请求系统账号,请检查参数配置[{}]", atomTranParams[2]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到请求系统账号,请检查参数配置[{}]", atomTranParams[2]));
        }
        //参数校验：判断目标系统账号是否为空
        String tgtAccNo = tranContext.getValue(atomTranParams[3]).toString();
        if (!StringUtils.hasText(tgtAccNo)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到目标系统账号,请检查参数配置[{}]", atomTranParams[3]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到目标系统账号,请检查参数配置[{}]", atomTranParams[3]));
        }
        String esscConfName = tranContext.getValue(atomTranParams[4]).toString();
        if (!StringUtils.hasText(esscConfName)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到秘钥配置名称,请检查参数配置[{}]", atomTranParams[4]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到秘钥配置名称,请检查参数配置[{}]", atomTranParams[4]), null);
        }
        // 获取完整密钥名
        String tgtZPKname = esscAssistant.getEsscKeyName(esscConfName);
        //参数校验：判断调用方HTTP头中的验签公钥名是否上送
        String edspMacKey = tranContext.getInHeaderScope().get(Constants.SC_ESP_MAC_KEY);
        if (!StringUtils.hasText(edspMacKey)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "调用方HTTP头中的验签公钥名为空[{}}]", Constants.SC_ESP_MAC_KEY);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("调用方HTTP头中的验签公钥名为空[{}]", Constants.SC_ESP_MAC_KEY));
        }
        this.convertPinCebToCups8583(pinCipher, pinKeyCipher, srcAccNo, tgtAccNo, edspMacKey, tgtZPKname, scopeValUnit);
    }


    /**
     * @param pinCipher    :PIN密文
     * @param pinKeyCipher ：PIN密钥密文
     * @param srcAccNo     ：请求方PIN种子
     * @param tgtAccNo     ：银联PIN种子
     * @param edspMacKey   ：源系统的验签公钥名
     * @param scopeValUnit ：本原子交易上下文：pinCipher：银联（8583规范）的PIN密文
     * @throws Exception 现在没有转pin种子的场景，确认账号入参保留几个
     */
    protected void convertPinCebToCups8583(String pinCipher, String pinKeyCipher, String srcAccNo, String tgtAccNo,
            String edspMacKey, String tgtZPKname, ScopeValUnit scopeValUnit) throws Exception {
        // 从缓存中获取加密平台所需的密钥名
//        String fbspPinVk = esscAssistant.getEsscKeyName(FBSP_PIN_VK);
//        LOGGER.info("调用加密平台参数：pinKeyCipher={},edspMacKey={},pinCipher={},srcAccNo={},tgtAccNo={},fbspPinVk={},tgtZPKname={}", pinKeyCipher, edspMacKey, pinCipher, srcAccNo, tgtAccNo, fbspPinVk, tgtZPKname);
//        //调用加密平台转PIN，函数名:CebTransPINFromRdmZPKToZPK（随机PIN密钥加密的PIN转ZPK加密（323交易））
//        String szPINbyZPK = getEsscAPI().CebTransPINFromRdmZPKToZPK(pinKeyCipher, edspMacKey, pinCipher, srcAccNo, tgtAccNo, fbspPinVk, tgtZPKname);
//        //将返回值放到上下文，供后续流程使用
//        byte[] encodedBytes = Base64.getEncoder().encode(HexTools.decodeHex(szPINbyZPK));
//        MapUtils.setVal(scopeValUnit, Constants.PIN_CIPHER, new String(encodedBytes, StandardCharsets.UTF_8));
    }


}

package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.springframework.util.StringUtils;


import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 *
 * 原子交易功能：PIN转换(8583规范->行内)
 * 原子参数输入说明：
 * 入参：PIN密文|请求系统账号|目标系统账号|目标系统服务名|卡组织秘钥配置名称
 * 出参：pinKey(PIN密钥密文)、pinValue（PIN密文）
 * 举例：
 * ${in.data.pwdPinCode.pinValue}|${in.data.srcAccNo}|${in.data.tgtAccNo}|'FBSP-SUBNOA-GatewayService'|'618_mcs_mac_key_name'
 * 增强处理输入说明：
 * 无需配置
 * 错误码输入说明
 * 无需配置
 *
 * @创建人 fbsp
 * @创建时间 2025/6/11 18:01
 * @描述 PIN转换：8583规范->行内
 */
@Atom("fbsp.PinConvertCups8583ToBank")
public class PinConvertCups8583ToBank extends AbstractPinConvertImpl {

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
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为{},请检查参数配置[{}]", PARAM_NUM_FIVE, atomTranParam));
        }
        //从上下文获取PIN密文
        String pinCipher = tranContext.getValue(atomTranParams[0]).toString();
        if (!StringUtils.hasText(pinCipher)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到PIN密文,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到PIN密文,请检查参数配置[{}]", atomTranParams[0]));
        }
        //参数校验：判断请求系统账号是否为空
        String srcAccNo = tranContext.getValue(atomTranParams[1]).toString();
        if (!StringUtils.hasText(srcAccNo)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到请求系统账号,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到请求系统账号,请检查参数配置[{}]", atomTranParams[1]));
        }
        //参数校验：判断目标系统账号是否为空
        String tgtAccNo = tranContext.getValue(atomTranParams[2]).toString();
        if (!StringUtils.hasText(tgtAccNo)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到目标系统账号,请检查参数配置[{}]", atomTranParams[2]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到目标系统账号,请检查参数配置[{}]", atomTranParams[2]));
        }
        String serviceName = tranContext.getValue(atomTranParams[3]).toString();
        //参数校验：判断行内目标系统服务名是否为空
        if (!StringUtils.hasText(serviceName)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到行内目标系统服务名,请检查参数配置[{}]", atomTranParams[3]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到行内目标系统服务名,请检查参数配置[{}]", atomTranParams[3]));
        }
        String esscConfName = tranContext.getValue(atomTranParams[4]).toString();
        if (!StringUtils.hasText(esscConfName)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到秘钥配置名称,请检查参数配置[{}]", atomTranParams[4]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到秘钥配置名称,请检查参数配置[{}]", atomTranParams[4]), null);
        }
        // 获取完整密钥名
        String srcZPKname = esscAssistant.getEsscKeyName(esscConfName);
        //调用具体的PIN转换方法
        this.convertPinMcsToCeb(pinCipher, srcAccNo, tgtAccNo, serviceName, srcZPKname, scopeValUnit);
    }


    /**
     * 将万事网联8583规范加密方式的PIN转换为行内系统加密方式的PIN
     * 此方法主要解决不同系统间PIN加密方式不兼容的问题，通过调用加密平台的特定接口实现PIN的转换
     *
     * @param pinCipher      :PIN密文
     * @param srcAccNo       :万事网联8583规范PIN种子
     * @param tgtAccNo       ：行内系统PIN种子
     * @param tgtServiceName ：行内目标系统服务名
     * @param scopeValUnit   ：本原子交易上下文：pinKeyCipher：目的系统的PIN密钥密文，pinCipher：目标系统的PIN密文
     */
    protected void convertPinMcsToCeb(String pinCipher, String srcAccNo, String tgtAccNo, String tgtServiceName,
            String srcZPKname, ScopeValUnit scopeValUnit) throws Exception {
        // 从缓存中获取加密平台所需的密钥名
//        String fbspMacPk = esscAssistant.getEsscKeyName(FBSP_MAC_PK);
//        //打印调用加密平台的参数信息
//        LOGGER.info("调用加密平台参数：mcsPinKey={}, fbspMacPk={},pinCipher={},srcAccNo={},tgtAccNo={},tgtServiceName={}", srcZPKname, fbspMacPk, pinCipher, srcAccNo, tgtAccNo, tgtServiceName);
//        //调用加密平台转PIN，函数名:CebTransPINFromZPKToRdmZPK（将ZPK加密的PIN转为随机PIN密钥加密（322交易））
//        HashMap<String, String> map = getEsscAPI().CebTransPINFromZPKToRdmZPK(srcZPKname, fbspMacPk, pinCipher, srcAccNo, tgtAccNo, tgtServiceName);
//        //将返回值放到上下文，供后续流程使用
//        MapUtils.setVal(scopeValUnit, Constants.PIN_KEY_CIPHER, map.get("szCipertextbyPK"));
//        MapUtils.setVal(scopeValUnit, Constants.PIN_CIPHER, map.get("szPINbyRdmZPK"));
    }

}

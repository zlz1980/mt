package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.n.fbsp.atom.platform.seed.other.eccs.EsscAssistant;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 通用秘钥交换原子交易。
 * 原子交易参数
 * 入参：加密密钥|密钥检验值|卡组织秘钥配置名称
 * 举例：
 * ${in.data.keyData}|${in.data.keyVerifyData}|'618_mcs_mac_key_name'
 * <p>
 * 增强处理参数
 * 无需配置
 *
 * @author 
 * @create 2025/12/17 11:12
 */
@Atom("fbsp.CebSymmKeyStore")
public class CebSymmKeyStoreImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CebSymmKeyStoreImpl.class);
    @Autowired
    protected EsscAssistant esscAssistant;

    /**
     * 密钥交换功能实现
     *
     * @param tranContext  交易上下文，用于传递交易过程中的上下文信息
     * @param scopeValUnit 作用域值单元，用于存储和传递作用域内的变量值
     * @param flowUnit     流程单元，包含流程的参数信息
     * @return 返回原子结果对象，包含执行结果
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化原子结果对象
        AtomResult atomResult = new AtomResult();
        // 初始化结果标识为false
        /*boolean result = false;
        // 检查原子交易参数是否为空或未配置
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程传输参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 检查原子交易参数个数是否为2
        if (atomTranParams.length != PARAM_NUM_THREE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam), null);
        }

        String keyData = (String) tranContext.getValue(atomTranParams[0]);
        if (!StringUtils.hasText(keyData)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到加密密钥,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到加密密钥,请检查参数配置[{}]", atomTranParams[0]), null);
        }
        String keyVerifyData = (String) tranContext.getValue(atomTranParams[1]);
        if (!StringUtils.hasText(keyVerifyData)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到密钥检验值,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到密钥检验值,请检查参数配置[{}]", atomTranParams[1]), null);
        }
        String esscConfName = (String) tranContext.getValue(atomTranParams[2]);
        if (!StringUtils.hasText(keyData)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到秘钥配置名称,请检查参数配置[{}]", atomTranParams[2]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到秘钥配置名称,请检查参数配置[{}]", atomTranParams[2]), null);
        }
        try {
            // 获取完整密钥名
            String keyFullName = esscAssistant.getEsscKeyName(esscConfName);
            // 获取EsscAPI实例
            EsscAPI esscApi = esscAssistant.getEsscAPI();
            // 日志输出开始执行秘钥交换
            LOGGER.info("秘钥交换开始,秘钥名称[{}]", keyFullName);
            // 执行秘钥交换操作
            int esscResult = esscApi.CebStoreSymmKey(keyFullName, keyData, keyVerifyData);
            // 检查秘钥交换结果
            if (esscResult == 0) {
                // 如果秘钥交换成功，则记录日志并设置结果标识为true
                LOGGER.info("秘钥[{}]交换成功!", keyFullName);
                result = true;
            }
        } catch (Exception e) {
            // 如果发生异常，则记录错误日志并抛出异常
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "秘钥交换异常,异常信息[{}]!", e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("秘钥交换异常，异常信息[{}]!", e.getMessage()), null);
        } finally {
            // 无论是否成功，都设置keyStoreResult的值
            MapUtils.setVal(scopeValUnit, "keyStoreResult", result);
        }
        // 日志输出当前作用域信息
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);*/
        // 设置执行结果为成功
        atomResult.setRetType(RetType.SUCCESS);
        // 返回原子结果对象
        return atomResult;
    }
}

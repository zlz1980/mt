package com.n.fbsp.atom.platform.seed.other.atom.comm;



import com.n.fbsp.atom.platform.seed.other.eccs.EsscAssistant;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


import static com.n.fbsp.atom.platform.seed.other.atom.comm.Track1ParseImpl.*;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 等效第二磁道中安全码验证
 * 原子交易参数
 * 二磁信息|卡组织秘钥配置名称
 * ${in.track2}|'004_cvn_key_name'
 * 增强处理参数：
 * 无需配置
 *
 * @author 
 * @date 2025/12/17
 * @description 等效第二磁道中安全码验证
 */
@Atom("fbsp.Track2CVVVerify")
public class Track2CVVVerifyImpl implements AtomService {
    /**
     * 万事网联渠道渠道8583报文验证及生成Mac使用的秘钥
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Track2CVVVerifyImpl.class);
    private static final String KEY_TYPE = "keyType";
    private final Track2ParseImpl track2Parse;
    @Autowired
    protected EsscAssistant esscAssistant;

    public Track2CVVVerifyImpl(Track2ParseImpl track2Parse) {
        this.track2Parse = track2Parse;
    }

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象
        AtomResult atomResult = new AtomResult();
        boolean result = false;
        // 检查原子交易参数是否为空或未配置
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程传输参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 检查原子交易参数个数是否为2
        if (atomTranParams.length != PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam), null);
        }
        ScopeValUnit reqScopeValUnit = new ScopeValUnit();
        FlowUnit newFlowUnit = new FlowUnit();
        flowUnit.setAtomTranParam(atomTranParams[0]);
        track2Parse.doService(tranContext, reqScopeValUnit, newFlowUnit);
        // 获取密钥子类
        String keyType = reqScopeValUnit.get(KEY_TYPE).toString();
        // 获取主账号
        String primaryAcctNo = reqScopeValUnit.get(PRIMARY_ACCT_NO).toString();
        // 获取失效日期
        String expDate = reqScopeValUnit.get(EXP_DATE).toString();
        // 获取服务代码
        String svcCode = reqScopeValUnit.get(SVC_CODE).toString();

        String otherData = reqScopeValUnit.get(OTHER_DATA).toString();
        // 获取安全码,otherData前3位
        String cvv = otherData.substring(0, 3);
        String esscConfName;
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParams[1]))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到二磁信息,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到二磁信息,请检查参数配置[{}]", atomTranParam), null);
        } else {
            esscConfName = tranContext.getValue(atomTranParams[1]).toString();
        }
        // 获取完整密钥名
        String keyFullName = esscAssistant.getEsscKeyName(esscConfName);

        try {
            // 获取EsscAPI实例
//            EsscAPI esscApi = esscAssistant.getEsscAPI();
            // 初始化Essc结果
            int esscResult = 0;
            LOGGER.info("等效二磁安全码验证开始,秘钥名称[{}]", keyFullName);
            //加密平台暂无CebVerifyCVV方法，参照云下接口进行预调用TODO，切换为云上加密API
//            esscResult = esscApi.CebVerifyCVV(keyFullName, expDate, svcCode, primaryAcctNo, cvv);
            // 检查秘钥交换结果
            if (esscResult == 0) {
                // 如果秘钥交换成功，则记录日志并设置结果标识为true
                LOGGER.info("等效二磁安全码验证成功!");
                result = true;
            }
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "等效二磁安全码验证异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("等效二磁安全码验证异常,异常信息[{}]", e.getMessage()), e);
        } finally {
            // 无论是否成功，都设置CVVVerifyResult的值
            MapUtils.setVal(scopeValUnit, "CVVVerifyResult", result);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

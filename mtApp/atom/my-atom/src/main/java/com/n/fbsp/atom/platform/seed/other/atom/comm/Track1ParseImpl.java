package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.nantian.nbp.base.model.FlowUnit;
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

import java.util.LinkedHashMap;
import java.util.Map;

import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @author 
 * @date 2025/6/18
 * @description 解析一磁信息
 */
@Atom("fbsp.Track1Parse")
public class Track1ParseImpl implements AtomService {
    protected static final String PRIMARY_ACCT_NO = "primaryAcctNo";
    protected static final String EXP_DATE = "expDate";
    protected static final String SVC_CODE = "svcCode";
    protected static final String OTHER_DATA = "otherData";
    private static final Logger LOGGER = LoggerFactory.getLogger(Track1ParseImpl.class);
    private static final String NAME = "name";
    private static final String PREFIX = "%";
    private static final String SUFFIX = "?";

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程传输参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        if (atomTranParams.length != PARAM_NUM_ONE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam), null);
        }
        StringBuffer track1;
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParam))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到一磁信息,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到一磁信息,请检查参数配置[{}]", atomTranParam), null);
        } else {
            String track1Str = tranContext.getCtxVal(atomTranParam).getVal().toString();
            track1 = new StringBuffer(track1Str);
        }
        if (!track1.toString().startsWith(PREFIX) || !track1.toString().endsWith(SUFFIX)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "一磁信息格式错误,请检查[{}]", track1);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("一磁信息格式错误,请检查[{}]", track1), null);
        }
        Map<String, String> result = new LinkedHashMap<>();
        try {
            int pos = 2;
            int idx1 = track1.indexOf("^", pos);
            //主账号
            result.put(PRIMARY_ACCT_NO, track1.substring(pos, idx1));
            pos = idx1 + 1;
            int idx2 = track1.indexOf("^", pos);
            //姓名
            result.put(NAME, track1.substring(pos, idx2));
            pos = idx2 + 1;
            //失效日期
            result.put(EXP_DATE, track1.substring(pos, pos + 4));
            pos += 4;
            //服务代码
            result.put(SVC_CODE, track1.substring(pos, pos + 3));
            pos += 3;
            //附加数据 去掉末尾 ?
            result.put(OTHER_DATA, track1.substring(pos, track1.length() - 1));
            scopeValUnit.putAll(result);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "一磁信息解析异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("一磁信息解析异常,异常信息[{}]", e.getMessage()), e);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

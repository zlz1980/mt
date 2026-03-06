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

import java.util.*;


import static com.n.fbsp.atom.platform.seed.other.atom.comm.Track1ParseImpl.*;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @author 
 * @date 2025/6/18
 * @description 解析二磁信息
 */
@Atom("fbsp.Track2Parse")
public class Track2ParseImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Track2ParseImpl.class);
    private static final Set<String> TRACK2_SEPARATORS = new HashSet<>(Arrays.asList("=", "D", "d"));

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
        StringBuffer track2;
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParam))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到二磁信息,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到二磁信息,请检查参数配置[{}]", atomTranParam), null);
        } else {
            String track2Str = tranContext.getCtxVal(atomTranParam).getVal().toString();
            track2 = new StringBuffer(track2Str);
        }

        Map<String, String> result = new LinkedHashMap<>();
        try {
            // skip ;
            int pos = 0;
            int sepPos = -1;
            for (String sep : TRACK2_SEPARATORS) {
                int idx = track2.substring(0, 22).indexOf(sep, pos);
                if (idx > 0) {
                    sepPos = idx;
                    break;
                }
            }
            if (sepPos == -1) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未找到二磁信息合法分隔符,请检查[{}]", track2);
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未找到二磁信息合法分隔符,请检查[{}]", track2), null);
            }
            //主账号
            result.put(PRIMARY_ACCT_NO, track2.substring(pos, sepPos));
            //跳过字段分隔符
            pos = sepPos + 1;
            //失效日期
            result.put(EXP_DATE, track2.substring(pos, pos + 4));
            pos += 4;
            //服务代码
            result.put(SVC_CODE, track2.substring(pos, pos + 3));
            pos += 3;
            //附加数据 去掉 ?
            result.put(OTHER_DATA, track2.substring(pos, track2.length()));
            scopeValUnit.putAll(result);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "二磁信息解析异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("二磁信息解析异常,异常信息[{}]", e.getMessage()), e);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

}

package com.n.fbsp.atom.platform.seed.val;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.ev.PbVal;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 对List类型进行声明和元素追加操作
 * 单参(atomTranParam),声明list名称, list对应对象限定在tmp空间进行管理
 * 多参(paramList),对list中追加一条或多条元素的变量路径${scope.xxx}
 */

@Atom("base.PbTempList")
public class PbTempList implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbTempList.class);

    @Override
    public AtomResult doService(TranContext tranContext, final ScopeValUnit scopeValUnit, final FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        String fTranCode = tranContext.getFeTranCode();
        String listKeyName = StrUtils.trim(flowUnit.getAtomTranParam());
        if (!StringUtils.hasText(listKeyName)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "List变量名称不能为空,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("List变量名称不能为空,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        // 获取目标List
        List<Object> dstList;
        // 属于临时命名空间
        PbScope<Object> tmpScope = tranContext.getTmpScope();
        Object dstListObject = MapUtils.getVal(tmpScope, listKeyName);
        if (Objects.isNull(dstListObject)) {
            dstList = new LinkedList<>();
            MapUtils.setVal(tmpScope, listKeyName, dstList);
        } else {
            if (dstListObject instanceof List) {
                dstList = (List) dstListObject;
            } else {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数对应的值为非List类型,请检查参数配置[{}]", flowUnit.getAtomTranParam());
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数对应的值为非List类型,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
            }
        }

        List<String> paramList = flowUnit.getParamList();
        if (CollectionUtils.isEmpty(paramList)) {
            LOGGER.debug("paramList为空,不进行追加元素操作");
        } else {
            // 元素追加
            for (String param : paramList) {
                if (StringUtils.hasText(param)) {
                    PbVal pbVal = tranContext.getCtxVal(StrUtils.trim(param));
                    if (Objects.isNull(pbVal.getVal())) {
                        LOGGER.error(APP_ATOM_RUN_ERR_KEY + "变量路径[{}]对应的目标数据不存在,请检查参数配置[{}]", param, flowUnit.getAtomTranParam());
                        throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg("变量路径[{}]对应的目标数据不存在,请检查参数配置[{}]", param, flowUnit.getAtomTranParam()), null);
                    } else {
                        dstList.add(pbVal.getVal());
                    }
                }
            }
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

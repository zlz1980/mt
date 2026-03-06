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

import java.util.Map;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 卡信息获取
 * 获取cardbin,cardFlag,cardType
 *
 * @Author : 
 * @Date 2024/8/29 11:19
 **/
@Atom("fbsp.CardBinSelect")
public class CardBinSelectImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardBinSelectImpl.class);

//    @Autowired
//    private BusiApi busiApi;

    /**
     * 卡信息获取服务
     * <p>
     * 本方法用于从流程单元中获取或处理卡信息，并更新至作用域对象中。
     * 主要功能包括：
     * 1. 获取并设置卡信息（如卡号，卡标识，卡类型）。
     * 2. 在需要时，处理特定逻辑，如根据卡号获取卡信息等。
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象，用于在流程各阶段传递和存储值。
     * @param flowUnit     当前单元，包含执行服务所需的参数。
     * @throws FlowException 如果执行流程操作时出错。
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象
        AtomResult atomResult = new AtomResult();
        // 提取参数，判断是否初始化
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg(
                    "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String acctNo;
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParam))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到卡号,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg(
                    "未获取到卡号,请检查参数配置[{}]", atomTranParam), null);
        } else {
            acctNo = (String) tranContext.getCtxVal(atomTranParam).getVal();
            if (ObjectUtils.isEmpty(acctNo)) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "卡号为空,请检查参数配置[{}]", atomTranParam);
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg(
                        "卡号为空,请检查参数配置[{}]", atomTranParam), null);
            }
        }

        Map<String, Object> tableResult = null;//busiApi.getCardBin(acctNo);
        // 如果获取信息失败，设置处理结果为失败并返回信息未获取到的消息
        if (ObjectUtils.isEmpty(tableResult)) {
            LOGGER.debug("未获取卡号为[{}]的cardBin缓存信息 ", acctNo);
            atomResult.setRetType(RetType.FAILED);
            atomResult.setErrMsg("未获取到静态表缓存信息");
            return atomResult;
        }else{
            LOGGER.info("卡BIN获取成功:[{}]", tableResult);
        }
        // 数据库查询结果信息处理并写入当前作用域
        scopeValUnit.putAll(tableResult);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

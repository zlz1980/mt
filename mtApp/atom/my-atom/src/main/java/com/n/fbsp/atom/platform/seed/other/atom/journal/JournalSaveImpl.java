package com.n.fbsp.atom.platform.seed.other.atom.journal;


import com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo;
import com.n.fbsp.atom.platform.seed.other.service.IJournalService;
import com.n.fbsp.atom.platform.seed.other.utils.JournalUtils;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;


import static com.n.fbsp.atom.platform.seed.other.utils.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.TRAN_SCOPE_RESERVED_KEYS;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_D0002091;

/**
 * 将tran作用域下未写入数据库的流水对象写入数据库T_EB_JOURNAL表
 * 1.原子交易参数：
 * 无需配置
 * 2.增强处理参数（以 | 分割）：
 * 无需配置
 * 3.错误码及错误描述：
 * 无需配置
 */
@Atom("fbsp.JournalSave")
public class JournalSaveImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalSaveImpl.class);

    @Autowired
    private IJournalService journalService;

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        // 获取tran内部作用域
        PbScope<Object> tranScope = tranContext.getTranScope();
        if (!CollectionUtils.isEmpty(tranScope)) {
            for (Map.Entry<String, Object> entry : tranScope.entrySet()) {
                //判断是否为保留字，保留字则跳过
                if (TRAN_SCOPE_RESERVED_KEYS.contains(entry.getKey())) {
                    LOGGER.warn("保留字[{}],跳过处理", entry.getKey());
                    continue;
                }
                Object v = entry.getValue();
                if (v instanceof PbScope) {
                    PbScope<Object> journalScope = (PbScope<Object>) v;
                    // 更新流水表
                    try {
                        if (!ObjectUtils.isEmpty(journalScope.get(OPERATE_TYPE))) {
                            if (journalScope.get(OPERATE_TYPE).equals(OPERATE_TYPE_P)) {
                                TEbJournalPo tEbJournalPo = JournalUtils.buildJournalPo(journalScope, tranContext.getSysScope());
                                journalService.updateJournalInfo(tEbJournalPo);
                                journalScope.put(OPERATE_TYPE, OPERATE_TYPE_U);
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水信息保存异常,异常信息[{}]", e.getMessage(), e);
                        //调用异常时的降级处理
                        throw new FlowException(T_D0002091.getCode(), tranContext.getFeTranCode(), T_D0002091.getCodeMsg("流水保存异常,异常信息[{}]", e.getMessage()), e);
                    }
                }
            }
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

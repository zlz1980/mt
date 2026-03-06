/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 * <<<<<<< .mine
 */

package com.n.fbsp.atom.platform.seed.db;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.TRANSACTION_STATUS;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.*;

/**
 * 提交DB事务，要求流程前序使用了DB事务开启的原子交易
 * 将中间所有对数据库的操作进行DB事务提交。
 *
 * @author JiangTaiSheng
 */

@Atom("base.TransactionCommit")
public class TransactionCommitImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCommitImpl.class);
    private final DataSourceTransactionManager transactionManager;

    public TransactionCommitImpl(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult result = new AtomResult();
        PbScope<Object> tranScope = tranContext.getTranScope();
        Object ts = tranScope.get(TRANSACTION_STATUS);
        if (Objects.isNull(ts) || !(ts instanceof TransactionStatus)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库事务未开启,无法提交,请检查前序流程!");
            throw new FlowException(T_C0003095.getCode(), T_C0003095.getCodeMsg("数据库事务未开启,无法提交,请检查前序流程!"));
        }
        TransactionStatus transactionStatus = (TransactionStatus) ts;
        try {
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "提交数据库事务异常,异常信息[{}],自动执行回滚!", e.getMessage());
            transactionManager.rollback(transactionStatus);
            throw new FlowException(T_C0003095.getCode(), tranContext.getFeTranCode(), T_C0003095.getCodeMsg("提交数据库事务异常,异常信息[{}]", e.getMessage()), e);
        } finally {
            tranScope.remove(TRANSACTION_STATUS);
        }
        LOGGER.info("数据库事务提交成功!");
        result.setRetType(RetType.SUCCESS);
        return result;
    }
}

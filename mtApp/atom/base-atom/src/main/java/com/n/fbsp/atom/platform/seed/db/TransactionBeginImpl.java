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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.TRANSACTION_STATUS;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 开启DB事物，流程后续所有对数据库操作的原子交易将被接管，
 * 不会直接commit生效，将会配合DB事务提交，DB事务回滚完成事务管理。
 *
 * @author JiangTaiSheng
 */

@Atom("base.TransactionBegin")
public class TransactionBeginImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionBeginImpl.class);
    private final DataSourceTransactionManager transactionManager;

    public TransactionBeginImpl(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult result = new AtomResult();
        PbScope<Object> tranScope = tranContext.getTranScope();
        Object ts = tranScope.get(TRANSACTION_STATUS);
        // 判断是否存在嵌套事务
        if (Objects.nonNull(ts)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库事务已开启,不允许嵌套,请检查前序流程!");
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("数据库事务已开启,不允许嵌套,请检查前序流程!"));
        }
        try {
            // 增加传播参数
            TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
            TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
            tranContext.getTranScope().put(TRANSACTION_STATUS, transactionStatus);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "开启数据库事务异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("开启数据库事务异常,异常信息[{}]", e.getMessage()), e);
        }
        LOGGER.info("数据库事务开启成功!");
        result.setRetType(RetType.SUCCESS);
        return result;
    }
}

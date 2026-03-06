/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.n.fbsp.atom.platform.seed.db;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.cache.server.init.model.SqlTemplateStatement;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.context.SagaAtomResult;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.saga.AbstractSagaAtomService;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.saga.SagaRollbackStep;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;
import com.nantian.nbp.utils.Timer;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.ObjectUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.RESULT_COUNT;
import static com.nantian.nbp.flow.engine.service.api.Constants.TRANSACTION_STATUS;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_D0002091;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 数据库增删改操作，通过读取原子交易参数从缓存中查询要执行的SQL模板，
 * 缓存信息来自t_pb_ext_sql_template表，通过增强处理来填充SQL模板中的参数，
 * 构建并执行SQL之后，进行数据库增删改操作，更新条数存在size变量中。
 *
 * @author liulei
 */
@Atom("base.DataDmlByTemplate")
public class DataDmlByTemplateImpl extends AbstractSagaAtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataDmlByTemplateImpl.class);
    private final SqlSessionFactory sqlSessionFactory;
    private final CacheClientApi cacheClientApi;


    public DataDmlByTemplateImpl(SqlSessionFactory sqlSessionFactory, CacheClientApi cacheClientApi) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.cacheClientApi = cacheClientApi;
    }

    /**
     * 1.原子交易参数（以 | 分割）：
     * 要执行的SQL模板的别名（AE页面中SQL配置功能模块中对应的SQL名字）
     * 2.增强处理参数（以 | 分割）：
     * SQL条件Key1| SQL条件Value1
     * SQL条件Key2| SQL条件Value2
     * SQL条件Key3| SQL条件Value3
     * SQL条件Keyn| SQL条件Valuen
     *
     * @param tranContext
     * @param scopeValUnit 当前步骤作用域
     * @param flowUnit     步骤单元信息
     * @return SagaAtomResult
     */
    @Override
    public AtomResult doService(TranContext tranContext, final ScopeValUnit scopeValUnit, final FlowUnit flowUnit) {
        SagaAtomResult result = new SagaAtomResult();
        int count = -1;
        // 获取外部交易码
        String fTranCode = tranContext.getFeTranCode();
        try {
            // 初始化计时器
            Long startTime = Timer.getStartTime();
            PbCommonAtomUtils.validateErrInfo(flowUnit, result);
            // 获取sqlStatement
            SqlTemplateStatement sqlStatement = getSqlStatement(flowUnit, tranContext);
            // 获取SQL参数
            Map<String, Object> sqlParams = DbUtils.getSqlParam(flowUnit.getParamList(), tranContext, scopeValUnit);
            // 记录获取SQL参数所花费的时间
            LOGGER.debug("获取SQL参数成功 sqlParamsUseTime[{}]ms", Timer.getUsedTime(startTime));
            // 执行sql
            count = executeSql(sqlStatement, sqlParams);
            // 记录执行SQL所花费的时间
            LOGGER.debug("数据库DML操作执行成功 executeSqlUseTime[{}]ms", Timer.getUsedTime(startTime));
            // 设置业务返回类型为成功
            result.setRetType(RetType.SUCCESS);
            if (count > 0) {
                result.setBusiRetType(RetType.SUCCESS);
                /* 在父类方法中根据是否设置了事务参数确定是否调用子类中的registerSagaStep */
                regSagaTransitionManager(tranContext, scopeValUnit, flowUnit);
            } else {
                result.setBusiRetType(RetType.FAILED);
            }
        } catch (FlowException e) {
            processUnKnowException(e, tranContext, scopeValUnit, flowUnit);
            // 重新抛出流程异常
            throw e;
        } catch (Exception e) {
            // 记录执行SQL时的异常信息
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库DML操作执行异常,异常信息[{}]", e.getMessage());
            // 设置错误信息
            processUnKnowException(e, tranContext, scopeValUnit, flowUnit);
            // 抛出流程异常
            throw new FlowException(T_X0005091.getCode(), fTranCode, T_X0005091.getCodeMsg("数据库DML操作执行异常,异常信息[{}]", e.getMessage()), e);
        } finally {
            LOGGER.info("数据库DML操作执行完成,size[{}]", count);
            // 保存执行SQL影响的行数
            scopeValUnit.put(RESULT_COUNT, count);
        }
        // 记录当前作用域信息
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        // 返回结果
        return result;
    }


    /**
     * 验证流程单元和范围值单元的参数是否有效
     *
     * @param flowUnit    流程单元，包含原子交易参数
     * @param tranContext 交易上下文访问接口
     */
    private SqlTemplateStatement getSqlStatement(FlowUnit flowUnit, TranContext tranContext) {
        String fTranCode = tranContext.getFeTranCode();
        // 检查原子交易参数是否为空或未配置
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 去除原子交易参数的前后空格
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 获取sqlId
        String sqlId = (String) tranContext.getValue(atomTranParam);
        // 获取sql信息
        SqlTemplateStatement sqlStatement = cacheClientApi.getSqlTemplateStatement(sqlId);
        // 检查是否成功获取SQL模板信息
        if (ObjectUtils.isEmpty(sqlStatement)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到SQL模板信息,请检查SQL模板参数[{}]", sqlId);
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg("未获取到SQL模板信息,请检查SQL模板参数配置[{}]", sqlId), null);
        }
        return sqlStatement;
    }

    /**
     * 执行SQL操作
     * 此方法用于执行数据库操作，并将执行结果更新到结果对象中
     * 它通过接收一个SagaAtomResult对象，一个SQL名称，一个SQL模板语句，查询参数和作用域值单位，
     * 来进行数据库操作，并处理可能的异常
     *
     * @param sqlStatement SQL模板语句
     * @param sqlParams    查询参数
     */
    private int executeSql(SqlTemplateStatement sqlStatement, Map<String, Object> sqlParams) {
        int retCount = -1;
        // 获取映射语句
        MappedStatement ms = sqlStatement.getMs();
        // 检查映射语句是否为空
        if (ObjectUtils.isEmpty(ms)) {
            // 记录错误日志并抛出异常
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库映射语句为空,请检查参数配置[{}]", sqlStatement.getSqlName());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("数据库映射语句为空,请检查参数配置[{}]", sqlStatement.getSqlName()));
        }
        SqlSession session = null;
        PreparedStatement statement = null;
        try {
            session = sqlSessionFactory.openSession(ExecutorType.SIMPLE);
            BoundSql boundSql = ms.getBoundSql(sqlParams);
            statement = DbUtils.getPreparedStatement(session, boundSql);
            // 设置参数
            DbUtils.setParams(statement, boundSql, sqlParams);
            // 记录调试信息
            LOGGER.debug("[{}]", statement.toString());
            //触发DML语句并返回受影响行数
            retCount = statement.executeUpdate();
        } catch (Exception e) {
            // 记录异常日志并抛出异常
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "executeSql数据库DML操作执行异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_D0002091.getCode(), T_D0002091.getCodeMsg("数据库DML操作执行异常,异常信息[{}]", e.getMessage()), e);
        } finally {
            try {
                if (Objects.nonNull(statement)) {
                    statement.close();
                }
            } catch (SQLException e) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库DML操作执行关闭资源异常,异常信息[{}]", e.getMessage(), e);
            }
            try {
                if (Objects.nonNull(session)) {
                    session.close();
                }
            } catch (Exception e) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库DML操作执行关闭资源异常,异常信息[{}]", e.getMessage(), e);
            }
        }
        return retCount;
    }

    /**
     * 注册Saga流程步骤
     * 此方法用于在满足某个条件时，注册一个Saga流程的回调步骤这个步骤用于准备进行回调时所需要的信息
     *
     * @param tranContext  管理Saga流程转换的管理器
     * @param scopeValUnit 用于获取交易代码的范围价值单位
     * @param flowUnit     流程单位，包含例如流程代码等信息
     */
    @Override
    public void registerSagaStep(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        try {
            //数据库saga步骤登记前先判定是否处于数据库事务控制流程中如果是，则报错不允许数据库事务管理嵌套saga事务管理
            PbScope<Object> tranScope = tranContext.getTranScope();
            Object ts = tranScope.get(TRANSACTION_STATUS);
            // 判断是否存在嵌套事务
            if (Objects.nonNull(ts) && ts instanceof TransactionStatus) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库事务已开启,不允许嵌套saga事务管理器登记回调信息,请检查流程步骤!");
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("数据库事务已开启,不允许嵌套saga事务管理器登记回调信息,请检查流程步骤!"), null);
            }
            // 记录日志，标记事务执行位置
            LOGGER.debug("saga事务管理器登记回调信息开始,当前流程步骤[{}],作用域[{}]", flowUnit.getAtomTranNo(), flowUnit.getAscope());
            // 实例化一个Saga回调步骤对象
            SagaRollbackStep rollbackStep = new SagaRollbackStep();
            String sagaParam = StrUtils.trim(flowUnit.getSagaParam());
            // 设置回调步骤的流程编号
            rollbackStep.setRbfTranCode(sagaParam);
            // 将输入参数转换为JSON格式字符串
            String inParamJson = JsonUtil.objToString(scopeValUnit);
            rollbackStep.setInParamJson(inParamJson);
            // 尝试向流程管理器添加回调步骤
            tranContext.getSagaTransitionManager().addStep(rollbackStep);
            LOGGER.debug("saga事务管理器登记回调信息完成");
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "saga事务管理器登记回调信息异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("saga事务管理器登记回调信息异常,异常信息[{}]", e.getMessage()), e);
        }
    }

    @Override
    protected void exceptionProcess(Exception e, TranContext tranContext, final ScopeValUnit scopeValUnit,
            final FlowUnit flowUnit) {
    }


}

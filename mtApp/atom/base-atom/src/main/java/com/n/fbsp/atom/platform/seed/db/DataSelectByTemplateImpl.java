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
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.utils.StrUtils;
import com.nantian.nbp.utils.Timer;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.RESULT_COUNT;
import static com.nantian.nbp.flow.engine.service.api.Constants.RESULT_INFO;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.*;

/**
 * 数据库查询操作，通过读取原子交易参数从缓存中获取要执行查询操作的SQL模板，
 * 缓存信息来自t_pb_ext_sql_template表，通过增强处理来填充SQL模板中的参数，
 * 构建并执行SQL之后，进行数据库查询操作，查询结果以数组的形式存储在当前作用域.list中，
 * 查询的数量存在size中。通过sql模板执行sql
 *
 * @author liulei
 * PBTest_findByParams sqlname
 * busiType|${req.busiType} 查询条件
 */
@Atom("base.DataSelectByTemplate")
public class DataSelectByTemplateImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSelectByTemplateImpl.class);
    private final SqlSessionFactory sqlSessionFactory;
    private final CacheClientApi cacheClientApi;

    public DataSelectByTemplateImpl(SqlSessionFactory sqlSessionFactory, CacheClientApi cacheClientApi) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.cacheClientApi = cacheClientApi;
    }

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        int count = -1;
        AtomResult atomResult = new AtomResult();
        String fTranCode = tranContext.getFeTranCode();
        try {
            Long startTime = Timer.getStartTime();
            PbCommonAtomUtils.validateErrInfo(flowUnit, atomResult);
            // 获取sqlStatement
            SqlTemplateStatement sqlStatement = getSqlStatement(flowUnit, tranContext);
            // 获取取SQL参数
            Map<String, Object> sqlParams = DbUtils.getSqlParam(flowUnit.getParamList(), tranContext, scopeValUnit);
            LOGGER.debug("获取SQL参数成功 sqlParamsUseTime[{}]ms", Timer.getUsedTime(startTime));
            // 执行sql
            count = executeSql(tranContext, sqlStatement, sqlParams, scopeValUnit);
            atomResult.setRetType(RetType.SUCCESS);
            LOGGER.debug("数据库SELECT操作执行成功 executeSqlUseTime[{}]ms", Timer.getUsedTime(startTime));
        } catch (FlowException e) {
            // 重新抛出流程异常
            throw e;
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库SELECT操作执行异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_X0005091.getCode(), fTranCode,
                                    T_X0005091.getCodeMsg("数据库SELECT操作执行异常,异常信息[{}]", e.getMessage()), e);
        } finally {
            LOGGER.info("数据库SELECT操作执行完成,size[{}]", count);
            scopeValUnit.put(RESULT_COUNT, count);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        return atomResult;
    }

    private SqlTemplateStatement getSqlStatement(FlowUnit flowUnit, TranContext tranContext) {
        //检查入参是否合法
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg(
                    "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 获取sqlId
        String sqlId = (String) tranContext.getValue(atomTranParam);
        // 获取sql信息
        SqlTemplateStatement sqlStatement = cacheClientApi.getSqlTemplateStatement(sqlId);
        if (ObjectUtils.isEmpty(sqlStatement)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到SQL模板信息,请检查SQL模板参数配置[{}]", sqlId);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg(
                    "未获取到SQL模板信息,请检查SQL模板参数配置[{}]", sqlId));
        }
        return sqlStatement;
    }

    /**
     * 执行SQL操作
     * 此方法负责根据提供的SQL模板语句、参数、以及自定义的范围值单元执行SQL查询或更新操作，并且处理日志和异常
     * 它演示了如何使用MyBatis框架执行SQL操作，包括准备和执行SQL语句、设置参数、以及处理结果集的完整过程
     *
     * @param sqlStatement SQL模板语句对象，包含待执行的SQL信息
     * @param sqlParams    查询参数映射，包含查询所需的参数键值对
     */
    private int executeSql(TranContext tranContext, SqlTemplateStatement sqlStatement, Map<String, Object> sqlParams,
            ScopeValUnit scopeValUnit) {
        int retCount = -1;
        String fTranCode = tranContext.getFeTranCode();
        MappedStatement ms = sqlStatement.getMs();
        if (ObjectUtils.isEmpty(ms)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库映射语句为空,请检查参数配置[{}]", sqlStatement.getSqlName());
            throw new FlowException(T_C0003092.getCode(), fTranCode,
                    T_C0003092.getCodeMsg("数据库映射语句为空,请检查参数配置[{}]", sqlStatement.getSqlName()), null);
        }
        SqlSession session = null;
        PreparedStatement statement = null;
        try {
            session = sqlSessionFactory.openSession(ExecutorType.SIMPLE);
            BoundSql boundSql = ms.getBoundSql(sqlParams);
            statement = DbUtils.getPreparedStatement(session, boundSql);
            // 设置参数
            DbUtils.setParams(statement, boundSql, sqlParams);
            ResultSet resultSet = statement.executeQuery();
            retCount = pushResult(resultSet, tranContext, scopeValUnit);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库SELECT操作执行异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_D0002091.getCode(), fTranCode,
                    T_C0003092.getCodeMsg("数据库SELECT操作执行异常,异常信息[{}]", e.getMessage()), e);
        } finally {
            try {
                if(Objects.nonNull(statement)) {
                    statement.close();
                }
            } catch (SQLException e) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库SELECT操作执行关闭资源异常,异常信息[{}]", e.getMessage(), e);
            }
            try {
                if(Objects.nonNull(session)) {
                    session.close();
                }
            } catch (Exception e) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库SELECT操作执行关闭资源异常,异常信息[{}]", e.getMessage(), e);
            }
        }
        return retCount;
    }


    /**
     * 设置执行结果
     * 此方法用于从数据库查询结果中提取数据，并将其存储到一个映射单元。
     * 它将每一项查询结果作为一个映射表（result map），按照查询字段和对应的值，
     * 添加进一个列表中。此方法还记录了查询到的结果数量，并将该数量信息更新到指定的范围单元。
     *
     * @param rs           数据库查询结果集
     * @param scopeValUnit 包含执行结果的范围值单元，用于后续将查询结果数量及结果本身存入
     */
    private int pushResult(ResultSet rs, TranContext tranContext, ScopeValUnit scopeValUnit) {
        String fTranCode = tranContext.getFeTranCode();
        // 初始化存储查询结果的列表
        List<Map<String, Object>> resultList = new ArrayList<>();
        // 记录已处理的行数
        int count = 0;
        // 创建存储查询结果的映射表
        Map<String, Object> resultMap;
        try {
            // 遍历查询结果
            while (rs.next()) {
                // 获取查询结果中的列数
                int columnCount = rs.getMetaData().getColumnCount();
                // 初始化一个新的映射表用于当前行数据，并为其容量提供一个预估值
                resultMap = new HashMap<>(columnCount);
                // 遍历并处理每一列的数据
                for (int colIdx = 0; colIdx < columnCount; colIdx++) {
                    // 获取当前列的标签
                    String columnLable = rs.getMetaData().getColumnLabel(colIdx + 1);
                    // 记录当前列的标签和值
                    LOGGER.trace("columnLable [{}], value [{}]", columnLable, rs.getObject(columnLable));
                    // 将当前列的标签和值添加到映射表中
                    resultMap.put(columnLable, rs.getObject(columnLable));
                }
                // 将映射表添加到结果列表中
                resultList.add(resultMap);
                // 增加处理的记录数计数器
                count++;
            }
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "数据库查询结果处理异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_C0003092.getCode(), fTranCode,
                                    T_C0003092.getCodeMsg("数据库查询结果处理异常,异常信息[{}]", e.getMessage()), e);
        }
        // 将查询结果和数量信息更新到指定单元中
        scopeValUnit.put(RESULT_INFO, resultList);
        return count;
    }

}

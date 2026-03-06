package com.n.fbsp.atom.platform.seed.db;

import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.StrUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 数据库操作工具类
 *
 * @author liulei
 */
public class DbUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtils.class);

    /**
     * 获取查询参数
     *
     * @param paras        参数
     * @param tranContext
     * @param scopeValUnit 作用域单元
     * @return 查询对象
     */
    public static Map<String, Object> getSqlParam(List<String> paras, TranContext tranContext,
            ScopeValUnit scopeValUnit) {
        String paramStr;
        String[] paramArr;
        if (CollectionUtils.isEmpty(paras)) {
            return scopeValUnit;
        }
        for (String para : paras) {
            paramStr = para;
            if (!StringUtils.hasText(paramStr)) {
                continue;
            }
            paramArr = StringUtils.delimitedListToStringArray(para, PARAM_SPILT_FLAG);
            if (paramArr.length != 2) {
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("SQL操作输入参数格式不合法, 当前参数[{}]", para));
            }
            String keyName = StrUtils.trim(paramArr[0]);
            String valueExp = StrUtils.trim(paramArr[1]);
            Object val = tranContext.getCtxVal(valueExp).getVal();
            LOGGER.debug("KVSet keyName[{}],valueExp[{}],value[{}]", keyName, valueExp, val);
            /*
             * 由于scopeValUnit会作为参数传递至mybatis框架api的getBoundSql方法，此方法会将map.code等待路径的标签按照对象层级进行解析，
             * 所以这里的赋值操作需采用MapUtils专用方法，按路径构建对象层级
             */
            MapUtils.setVal(scopeValUnit, keyName, val);
        }
        return scopeValUnit;
    }

    /**
     * 设置查询参数
     *
     * @param statement   jdbc语句
     * @param boundSql    綁定SQL
     * @param queryParams 参数
     * @throws Exception 执行异常
     */
    public static void setParams(PreparedStatement statement, BoundSql boundSql,
            Map<String, Object> queryParams) throws Exception {
        // 获取参数映射列表
        List<ParameterMapping> paramsList = boundSql.getParameterMappings();
        // 如果参数列表为空，则直接返回
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }
        // 参数名
        String property;
        // map中的参数值
        Object mapParam;
        // 参数索引，从1开始
        int index = 1;
        // 遍历参数映射列表
        for (ParameterMapping parameter : paramsList) {
            if (parameter.getMode() == ParameterMode.OUT) {
                continue;
            }
            // 获取属性名称
            property = parameter.getProperty();
            // 如果存在附加参数
            if (boundSql.hasAdditionalParameter(property)) {
                mapParam = boundSql.getAdditionalParameter(property);
            } else {
                //参数值获取方式需与此源码文件中的getSqlParam方法赋值逻辑保持一致！
                mapParam = MapUtils.getVal(queryParams, property);
            }
            // 设置查询参数
            statement.setObject(index, mapParam);
            // 增加索引值
            index++;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("param [{}]", mapParam);
            }
        }
    }

    /**
     * 获取PreparedStatement
     * 此方法用于获取PreparedStatement，并绑定SQL语句
     *
     * @param session  SqlSession
     * @param boundSql BoundSql
     * @return PreparedStatement
     */
    public static PreparedStatement getPreparedStatement(SqlSession session, BoundSql boundSql) throws SQLException {
        Connection conn = session.getConnection();
        // 获取待绑定的SQL语句
        String sql = boundSql.getSql();
        LOGGER.debug("[{}]", sql);
        return conn.prepareStatement(sql);
    }
}

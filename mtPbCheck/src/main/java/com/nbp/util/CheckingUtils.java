package com.nbp.util;

import com.nbp.db.DbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 校验工具类
 */
public class CheckingUtils {

    private static final Logger logger = LoggerFactory.getLogger(CheckingUtils.class);

    /**
     * 校验条目数的sql集合，key为表名，value为按工程查看资源数量sql
     */
    private static final Map<String, String> TABLE_COUNT_SQL_MAP = new HashMap<String, String>(48){
        {
            // 根据工程id查询批转联表数量
            put("T_PB_BATCH","select count(1) from T_PB_BATCH t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'batch' and proid = ? and t1.crtproinfo = t.taskName and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询应用系统参数配置表数量
            put("T_PB_SYS_CFG","select count(1) from T_PB_SYS_CFG t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'sysCfg' and proid = ? and t1.crtproinfo = t.DEFNAME and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询工具bean数量
            put("T_PB_EXT_UTILS_BEAN","select count(1) from T_PB_EXT_UTILS_BEAN t where exists (select 1 from t_pb_project_info where crtprolv1 = 'utilBean' and proid = ? and crtproinfo = t.beanName and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询原子交易目录数量
            put("T_PB_CTRL_STREAM","select count(1) from T_PB_CTRL_STREAM t where exists (select 1 from t_pb_project_info where crtprolv1 = 'AtomTran' and proid = ? and chnlno = t.STREAMCODE and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询原子交易数量
            put("T_PB_ATOM_TRAN","select count(1) from T_PB_ATOM_TRAN t where exists (select 1 from t_pb_project_info where crtprolv1 = 'AtomTran' and proid = ? and crtproinfo = t.ATOMTRANCODE and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询规则组数量
            put("T_PB_EXT_RULE_GROUP","select count(1) from T_PB_EXT_RULE_GROUP t where exists (select 1 from t_pb_project_info where crtprolv1 = 'ruleGroup' and proid = ? and crtproinfo = t.bizType and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询规则设置数量
            put("T_PB_EXT_RULE_SET","select count(1) from T_PB_EXT_RULE_SET t where exists (select 1 from t_pb_project_info where crtprolv1 = 'ruleSet' and proid = ? and crtproinfo = concat(t.bizType,'_',t.groupid,'_',t.ruleid) and chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询规则数量
            put("T_PB_EXT_RULE","select count(1) from T_PB_EXT_RULE t where exists (select 1 from t_pb_project_info where crtprolv1 = 'rule' and proid = ? and crtproinfo = concat(t.bizType,'_',t.ruleName) and chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询sql配置数量
            put("T_PB_EXT_SQL_TEMPLATE","select count(1) from T_PB_EXT_SQL_TEMPLATE t where exists (select 1 from t_pb_project_info where crtprolv1 = 'sqlTemplate' and proid = ? and crtproinfo = t.sqlName and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询静态缓存表数量
            put("T_PB_STATICTBL_APP","select count(1) from T_PB_STATICTBL_APP t where exists (select 1 from t_pb_project_info where crtprolv1 = 'staticTbl' and proid = ? and crtproinfo = concat(t.cacheid,'_',t.keyname) and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询错误码数量
            put("T_PB_EXT_ERROR_CODE","select count(1) from T_PB_EXT_ERROR_CODE t where exists (select 1 from t_pb_project_info where crtprolv1 = 'ErrorCode' and proid = ? and crtproinfo = t.errorcode and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询错误码类型数量
            put("T_PB_EXT_ERROR_TYPE","select count(1) from T_PB_EXT_ERROR_TYPE t where exists (select 1 from t_pb_project_info where crtprolv1 = 'errorType' and proid = ? and crtproinfo = t.typeid and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询http通讯数量
            put("T_PB_EXT_HTTP_CONF","select count(1) from T_PB_EXT_HTTP_CONF t where exists (select 1 from t_pb_project_info where crtprolv1 = 'httpConf' and proid = ? and crtproinfo = t.serName and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询业务种类数量
            put("T_PB_BUSI_TYPE","select count(1) from T_PB_BUSI_TYPE t where exists (select 1 from t_pb_project_info where (crtprolv1 = 'Module' or crtprolv1 = 'TranCode') and proid = ? and chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询内部交易码数量
            put("T_PB_TRAN_CODE","select count(1) from T_PB_TRAN_CODE t where exists (select 1 from t_pb_project_info where (crtprolv1 = 'Module' or crtprolv1 = 'TranCode') and proid = ? and crtproinfo = t.trancode and chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询交易流程数量
            put("T_PB_FLOW","select count(1) from T_PB_FLOW t where exists (select 1 from t_pb_project_info where (crtprolv1 = 'Module' or crtprolv1 = 'TranCode') and proid = ? and crtproinfo = t.trancode and chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询交易流程参数数量
            put("T_PB_FLOW_PARA","select count(1) from T_PB_FLOW_PARA t where exists (select 1 from t_pb_project_info where (crtprolv1 = 'Module' or crtprolv1 = 'TranCode') and proid = ? and crtproinfo = t.trancode and chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询交易流程接口规范数量
            put("T_PB_EXT_API","select count(1) from T_PB_EXT_API t where exists (select 1 from t_pb_project_info where crtprolv1 = 'extApi' and proid = ? and crtproinfo = concat(t.BUSITYPE, concat('_', t.apicode)) and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询渠道数量
            put("T_PB_CHNL","select count(1) from T_PB_CHNL t where exists (select 1 from t_pb_project_info where crtprolv1 = 'FTranCode' and proid = ? and chnlno = t.CHNLNO and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询外部交易码数量
            put("T_PB_TRAN_PKG","select count(1) from T_PB_TRAN_PKG t where exists (select 1 from t_pb_project_info where crtprolv1 = 'FTranCode' and proid = ? and crtproinfo = t.FTRANCODE and chnlno = t.CHNLNO and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询内外部交易码映射数量
            put("T_PB_TRAN_CODE_CONV","select count(1) from T_PB_TRAN_CODE_CONV t where exists (select 1 from t_pb_project_info where crtprolv1 = 'FTranCode' and proid = ? and crtproinfo = t.FTRANCODE and chnlno = t.CHNLNO and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询工程目录数量
            put("T_PB_PROJECT_DIR","select count(1) from T_PB_PROJECT_DIR t where proid = ?");
            // 根据工程id查询工程信息数量
            put("T_PB_PROJECT","select count(1) from T_PB_PROJECT t where proid = ?");
            // 根据工程id查询工程相关资源数量
            put("T_PB_PROJECT_INFO","select count(1) from T_PB_PROJECT_INFO t where proid = ?");
            // 根据工程id查询网关静态配置数量
            put("T_PB_STATICTBL_GW","select count(1) from T_PB_STATICTBL_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'staticTblGw' and proid = ? and crtproinfo = concat(t.cacheid,'_',t.keyname) and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询网关规则组数量
            put("T_PB_EXT_RULE_GROUP_GW","select count(1) from T_PB_EXT_RULE_GROUP_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'gwRuleGroup' and proid = ? and crtproinfo = t.bizType and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询网关规则配置数量
            put("T_PB_EXT_RULE_SET_GW","select count(1) from T_PB_EXT_RULE_SET_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'gwRuleSet' and proid = ? and crtproinfo = concat(t.bizType,'_',t.groupid,'_',t.ruleid) and chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询网关规则数量
            put("T_PB_EXT_RULE_GW","select count(1) from T_PB_EXT_RULE_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'gwRule' and proid = ? and crtproinfo = concat(t.bizType,'_',t.ruleName) and chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询网关系统配置数量
            put("T_PB_SYS_CFG_GW","select count(1) from T_PB_SYS_CFG_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'sysCfgGw' and proid = ? and crtproinfo = t.DEFNAME and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询网关灰度路由控制数量
            put("T_PB_TESTROUTE_GW","select count(1) from T_PB_TESTROUTE_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'testRoute' and proid = ? and crtproinfo = concat(t.REQCHNL,'_',t.FTRANCODE) and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询网关路由控制数量
            put("T_PB_TRANROUTE_GW","select count(1) from T_PB_TRANROUTE_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'tranRouteGw' and proid = ? and crtproinfo = concat(t.REQCHNL,'_',t.FTRANCODE) and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询网关异常信息返回数量
            put("T_PB_EXCEPTION_GW","select count(1) from T_PB_EXCEPTION_GW t where exists (select 1 from t_pb_project_info where crtprolv1 = 'exceptionGw' and proid = ? and crtproinfo = CONCAT(t.reqChnl,'_',t.code) and (protype = 'NEW' or protype='EDIT'))");
            // 根据工程id查询组件目录数量
            put("T_PB_EXT_MODULE_DIR","select count(1) from T_PB_EXT_MODULE_DIR t where exists (select 1 from t_pb_project_dir t1 where (PROTYPE = 'Module' or PROTYPE = 'Module_000' or PROTYPE = 'Module_INF') and proid = ? and t1.DIRCODE = t.TYPE and (STYLE = 'NEW' or STYLE='EDIT'))");
            // 根据工程id查询组件目录与组件关系信息数量
            put("T_PB_EXT_MODULE","select count(1) from T_PB_EXT_MODULE t where exists (select 1 from t_pb_project_info t1 where crtprolv1 = 'Module' and chnlno = 'AAA' and proid = ? and t1.crtproinfo = t.trancode and (PROTYPE = 'NEW' or PROTYPE='EDIT'))");
        }
    };
    /**
     * 全局参数，存储导出的表名和条目数
     */
    private static final Map<String, Integer> TABLE_NUM_MAP = new HashMap<>(48);

    /**
     * 存储表名和条目数
     * @param tableName 表名
     * @param num 条目数
     */
    public static void putTableNum(String tableName, Integer num){
        TABLE_NUM_MAP.merge(tableName, num, Integer::sum);
    }

    /**
     * 清空表名和条目数
     */
    public static void clearTableNum(){
        TABLE_NUM_MAP.clear();
    }

    /**
     * 检查导出数据与数据库中条目数是否一致
     * @param proId 工程id
     */
    public static void checking(Connection conn, String proId) {
        TABLE_NUM_MAP.forEach((tableName, num) -> {
            int tableNum = DbConnection.executeQueryCountSql(conn, TABLE_COUNT_SQL_MAP.get(tableName), proId);
            if (tableNum == num) {
                logger.info("工程{}表{}，导出数据与数据库中条目数一致，导出{}条",proId, tableName, tableNum);
            } else {
                logger.error("工程{}表{}，导出数据与数据库中条目数不一致，导出{}条，数据库中{}条",proId, tableName, num, tableNum);
            }
        });
    }

}

package com.nbp.util;

public class Tips {

    /** 导出说明 */
    public static final String CHECK_OUT_EXPLAIN =
            "全库导出:                    java -jar PbCheckOut.jar -A [目录]\n" +
                "全库导出网关相关信息:        java -jar PbCheckOut.jar -GW_A [目录]\n" +
                "全库导出应用相关信息:        java -jar PbCheckOut.jar -APP_A [目录]\n" +
                "按工程ID导出:                java -jar PbCheckOut.jar -E [工程ID1]|[工程ID2]|... [目录]\n" +
                "按工程ID导出网关相关信息:    java -jar PbCheckOut.jar -GW_E [工程ID1]|[工程ID2]|... [目录]\n" +
                "按工程ID导出应用相关信息:    java -jar PbCheckOut.jar -APP_E [工程ID1]|[工程ID2]|... [目录]\n" +
                "单项导出批量任务:            java -jar PbCheckOut.jar -S -BATCH [TASKNAME1]|[TASKNAME2]|... [目录]\n" +
                "单项导出系统参数:            java -jar PbCheckOut.jar -S -SYSCFG [DNFNAME1]|[DNFNAME2]|... [目录]\n" +
                "单项导出工具类:              java -jar PbCheckOut.jar -S -UTILBEAN [BEANNAME1]|[BEANNAME2]|... [目录]\n" +
                "单项导出原子交易:            java -jar PbCheckOut.jar -S -ATOM [ATOMTRANCODE1]|[ATOMTRANCODE2]|... [目录]\n" +
                "单项导出应用规则组:          java -jar PbCheckOut.jar -S -RULE [BIZTYPE1]|[BIZTYPE2]|... [目录]\n" +
                "单项导出SQL配置:             java -jar PbCheckOut.jar -S -SQL [SQLNAME1]|[SQLNAME2]|... [目录]\n" +
                "单项导出静态缓存:            java -jar PbCheckOut.jar -S -STATICTBL [CACHEID1]|[CACHEID2]|... [目录]\n" +
                "单项导出错误码:              java -jar PbCheckOut.jar -S -ERRORCODE [ERRORCODE1]|[ERRORCODE2]|... [目录]\n" +
                "单项导出错误码类型:          java -jar PbCheckOut.jar -S -ERRORTYPE TYPEID1|TYPEID1|... 目录\n" +
                "单项导出HTTP通讯:            java -jar PbCheckOut.jar -S -HTTP [SERNAME1]|[SERNAME2]|... [目录]\n" +
                "单项导出内部交易:            java -jar PbCheckOut.jar -S -TRANCODE [BUSITYPE1]|[BUSITYPE2]|... [TRANCODE1]|[TRANCODE2]|... [目录]\n" +
                "单项导出接口规范:            java -jar PbCheckOut.jar -S -EXTAPI [BUSITYPE1]|[BUSITYPE2]|... [APICODE1]|[APICODE2]|... [目录]\n" +
                "单项导出外部交易:            java -jar PbCheckOut.jar -S -FTRANCODE [CHNLNO1]|[CHNLNO2]|... [FTRANCODE1]|[FTRANCODE2]|... [目录]\n" +
                "单项导出工程信息:            java -jar PbCheckOut.jar -S -P [PROID1]|[PROID2]|... [目录]\n" +
                "单项导出处理模块目录:        java -jar PbCheckOut.jar -S -MODULE MODULETYPE1|MODULETYPE2|... [目录]\n" +
                "单项导出网关规则组:          java -jar PbCheckOut.jar -S -GWRULE [BIZTYPE1]|[BIZTYPE2]|... [目录]\n" +
                "单项导出网关静态缓存:        java -jar PbCheckOut.jar -S -GWSTATICTBL [CACHEID1]|[CACHEID2]|... [目录]\n" +
                "单项导出网关系统参数:        java -jar PbCheckOut.jar -S -GWSYSCFG [DNFNAME1]|[DNFNAME2]|... [目录]\n" +
                "单项导出网关灰度路由控制表:  java -jar PbCheckOut.jar -S -GWTESTROUTE [CHNLNO1]|[CHNLNO2]|... [FTRANCODE1]|[FTRANCODE2]|... [目录]\n" +
                "单项导出网关路由控制表:      java -jar PbCheckOut.jar -S -GWTRANROUTE [CHNLNO1]|[CHNLNO2]|... [FTRANCODE1]|[FTRANCODE2]|... [目录]\n" +
                "单项导出网关异常默认报文返回:java -jar PbCheckOut.jar -S -GWEXCEPTION [CHNLNO1]|[CHNLNO2]|... [CODE1]|[CODE2]|... [目录]";
    /** 导入说明 */
    public static final String CHECK_IN_EXPLAIN = "导入:java -jar PbCheckIn.jar [目录]";
}

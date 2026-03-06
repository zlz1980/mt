/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.api;

/**
 * 常量类
 * @author JiangTaiSheng
 */
public class FeConstants {
    /** 渠道号Key */
    public static final String CHNL_NO = "chnlNo";
    /** 外部交易码Key */
    public static final String F_TRAN_CODE = "fTranCode";
    /** 内部交易码Key */
    public static final String TRAN_CODE = "tranCode";
    /** 交易类型Key */
    public static final String TRAN_TYPE = "tranType";
    /** 业务种类Key */
    public static final String BIZ_TYPE = "bizType";
    /** 流水号Key */
    public static final String BIZ_ID = "bizId";

    /** 上下文对象 */
    public static final String FE_CONTEXT = "FeContext";

    /** 交易日期Key */
    public static final String TRAN_DATE = "tranDate";
    /** 交易时间Key */
    public static final String TRAN_TIME = "tranTime";
    /** 错误码Key */
    public static final String ERR_CODE = "errCode";
    /** 错误信息Key */
    public static final String ERR_MSG = "errMsg";
    /** 业务返回类型Key */
    public static final String BUSI_RET_TYPE = "busiRetType";
    /** 交易日志Key */
    public static final String PB_LOG = "pbLog";
    /** 上下文Key */
    public static final String CTX_FLAG = "ctx";
    /** 输入体作用域Key */
    public static final String IN_SCOPE = "in";
    /** 输入头作用域Key */
    public static final String IN_HEADER_SCOPE = "inHeader";
    /** 交易作用域Key */
    public static final String TRAN_SCOPE = "tran";
    /** 输出体作用域Key */
    public static final String OUT_SCOPE = "out";
    /** 输出头作用域Key */
    public static final String OUT_HEADER_SCOPE = "outHeader";
    /** 系统作用域Key */
    public static final String SYS_SCOPE = "sys";
    /** 临时作用域Key */
    public static final String TMP_SCOPE = "tmp";
    /** MAP类型初始化Key */
    public static final int MAP_INIT = 16;
    /** 校验JsonSchema开关 */
    public static final String JSON_SCHEMA_FLAG = "JsonSchemaFlag";
    public static final String REQ_JSON_SCHEMA_TYPE = "0";
    public static final String RES_JSON_SCHEMA_TYPE = "1";
    public static final String ERR_JSON_SCHEMA_TYPE = "2";

    public static final String ERR_EXCEPTION_TYPE = "0";
    public static final String UNN_EXCEPTION_TYPE = "1";

    public static final String EBAC_BUSI_KEY = "ebac-busikey";
    public static final String ERROR_TRAN_STATUS = "E";
    public static final String SUCCESS_TRAN_STATUS = "S";

}

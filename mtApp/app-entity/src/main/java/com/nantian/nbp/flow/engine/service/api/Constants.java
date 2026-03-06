/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.flow.engine.service.api;

import static com.nantian.nbp.flow.engine.service.api.FeConstants.CTX_FLAG;

/**
 * seed下原子交易常量类
 * @author JiangTaiSheng
 */
public class Constants {
    public static final String EL_CTX_FLAG = "#"+CTX_FLAG;
    public static final String PARAM_SPILT_FLAG = "|";
    public static final String URL_SPLIT_FLAG = "/";
    public static final String PARAM_DOT_SPILT = ".";
    public static final String PARAM_CONN_SPILT = "-";
    public static final String PARAM_COMMA_SPILT = ",";
    public static final String ZERO_PARAM = "0";
    public static final String INIT_RET_VAL = "00000";
    public static final String PARAM_ASSIGNMENT_SPILT = "=";
    public static final String PARAM = "param|";
    public static final String RESULT_INFO = "list";
    public static final String RESULT_COUNT = "size";

    public static final String HTTP_CONF= "httpConf";

    public static final String ERGODIC_IDX = "ERGODIC_NEXT_IDX_";

    public static final String VAR_START_FLAG = "${";
    public static final String VAR_END_FLAG = "}";

    public static final String VAR_FLAG = "#ctx.";

    public static final String TRANSACTION_STATUS = "TransactionStatus";
    public static final int PARAM_NUM_ZERO = 0;
    public static final int PARAM_NUM_ONE = 1;

    public static final int PARAM_NUM_TWO = 2;
    public static final int PARAM_NUM_THREE = 3;
    public static final int PARAM_NUM_FOUR = 4;
    public static final int PARAM_NUM_FIVE = 5;
    public static final int PARAM_NUM_SIX = 6;
    public static final int PARAM_NUM_SEVEN = 7;
    public static final int PARAM_NUM_EIGHT = 8;

    public static final String IGNORE_EXCEPTION = "Y";

    public static final String APP_ATOM_RUN_ERR_KEY="APPATOMRUNERROR: ";
    public static final String APP_ENGINE_RUN_ERR_KEY="APPENGINERUNERROR: ";
    public static final String APP_MQ_PRODUCER_ERR_KEY="APPMQPRODUCERERROR: ";

    public static final String WHILE_PARAM_SPILT_FLAG = ":";

    public static final String RECORD_STEP_NO_KEY = "RECORD_STEP_NO";

    public static final String ON = "ON";

    public static final String TRAN_SCOPE_RESERVED_KEYS = "";
}

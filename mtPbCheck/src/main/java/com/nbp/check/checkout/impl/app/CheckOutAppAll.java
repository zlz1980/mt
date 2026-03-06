package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 全库导出应用相关表导出类
 */
public class CheckOutAppAll extends CheckOutExecutor {
    /**
     * 全库导出应用相关表执行器列表
     */
    private final List<CheckOutExecutor> CHECK_OUT_LIST = new ArrayList<CheckOutExecutor>(15){
        {
            add(new CheckOutBatch());
            add(new CheckOutSysCfg());
            add(new CheckOutAtom());
            add(new CheckOutRuleGroup());
            add(new CheckOutSqlTemplate());
            add(new CheckOutStatictbl());
            add(new CheckOutErrorCode());
            add(new CheckOutErrorType());
            add(new CheckOutHttpConf());
            add(new CheckOutTran());
            add(new CheckOutChnl());
            add(new CheckOutProject());
            add(new CheckOutUtilBean());
            add(new CheckOutModule());
            add(new CheckOutExtApi());
        }
    };

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        this.CHECK_OUT_LIST.forEach(checkOutExecutor -> checkOutExecutor.execute(conn, proIds, params1, params2, filePath));
    }

}

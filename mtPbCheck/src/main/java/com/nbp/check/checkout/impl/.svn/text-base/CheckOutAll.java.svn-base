package com.nbp.check.checkout.impl;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.check.checkout.impl.app.CheckOutAtom;
import com.nbp.check.checkout.impl.app.CheckOutBatch;
import com.nbp.check.checkout.impl.app.CheckOutChnl;
import com.nbp.check.checkout.impl.app.CheckOutErrorCode;
import com.nbp.check.checkout.impl.app.CheckOutErrorType;
import com.nbp.check.checkout.impl.app.CheckOutExtApi;
import com.nbp.check.checkout.impl.app.CheckOutHttpConf;
import com.nbp.check.checkout.impl.app.CheckOutModule;
import com.nbp.check.checkout.impl.app.CheckOutProject;
import com.nbp.check.checkout.impl.app.CheckOutRuleGroup;
import com.nbp.check.checkout.impl.app.CheckOutSqlTemplate;
import com.nbp.check.checkout.impl.app.CheckOutStatictbl;
import com.nbp.check.checkout.impl.app.CheckOutSysCfg;
import com.nbp.check.checkout.impl.app.CheckOutTran;
import com.nbp.check.checkout.impl.app.CheckOutUtilBean;
import com.nbp.check.checkout.impl.gw.CheckOutGwException;
import com.nbp.check.checkout.impl.gw.CheckOutGwRuleGroup;
import com.nbp.check.checkout.impl.gw.CheckOutGwStatictbl;
import com.nbp.check.checkout.impl.gw.CheckOutGwSysCfg;
import com.nbp.check.checkout.impl.gw.CheckOutGwTestRoute;
import com.nbp.check.checkout.impl.gw.CheckOutGwTranRoute;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 全库导出相关表导出类
 */
public class CheckOutAll extends CheckOutExecutor {
    /**
     * 全库导出相关表执行器列表
     */
    private final List<CheckOutExecutor> checkOutList = new ArrayList<CheckOutExecutor>(21){
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
            add(new CheckOutExtApi());
            add(new CheckOutGwStatictbl());
            add(new CheckOutGwRuleGroup());
            add(new CheckOutGwSysCfg());
            add(new CheckOutGwTestRoute());
            add(new CheckOutGwTranRoute());
            add(new CheckOutGwException());
            add(new CheckOutModule());
        }
    };

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        this.checkOutList.forEach(checkOutExecutor -> checkOutExecutor.execute(conn, proIds, params1, params2, filePath));
    }

}

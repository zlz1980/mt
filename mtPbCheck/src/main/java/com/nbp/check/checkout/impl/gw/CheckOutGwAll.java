package com.nbp.check.checkout.impl.gw;

import com.nbp.check.checkout.CheckOutExecutor;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 全库导出网关相关表导出类
 */
public class CheckOutGwAll extends CheckOutExecutor {
    /**
     * 全库导出网关相关表执行器列表
     */
    private final List<CheckOutExecutor> checkOutGWList = new ArrayList<CheckOutExecutor>(6){
        {
            add(new CheckOutGwStatictbl());
            add(new CheckOutGwRuleGroup());
            add(new CheckOutGwSysCfg());
            add(new CheckOutGwTestRoute());
            add(new CheckOutGwTranRoute());
            add(new CheckOutGwException());
        }
    };

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        this.checkOutGWList.forEach(CheckOutExecutor -> execute(conn, proIds, params1, params2, filePath));
    }

}

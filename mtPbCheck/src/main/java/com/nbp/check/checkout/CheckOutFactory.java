package com.nbp.check.checkout;

import com.nbp.check.checkout.impl.CheckOutAll;
import com.nbp.check.checkout.impl.CheckOutByProject;
import com.nbp.check.checkout.impl.app.CheckOutAppAll;
import com.nbp.check.checkout.impl.app.CheckOutAppByProject;
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
import com.nbp.check.checkout.impl.gw.CheckOutGwAll;
import com.nbp.check.checkout.impl.gw.CheckOutGwByProject;
import com.nbp.check.checkout.impl.gw.CheckOutGwException;
import com.nbp.check.checkout.impl.gw.CheckOutGwRuleGroup;
import com.nbp.check.checkout.impl.gw.CheckOutGwStatictbl;
import com.nbp.check.checkout.impl.gw.CheckOutGwSysCfg;
import com.nbp.check.checkout.impl.gw.CheckOutGwTestRoute;
import com.nbp.check.checkout.impl.gw.CheckOutGwTranRoute;

import java.util.HashMap;
import java.util.Map;

import static com.nbp.util.Constants.EXPORT_ALL;
import static com.nbp.util.Constants.EXPORT_APP_ALL;
import static com.nbp.util.Constants.EXPORT_APP_BY_PROJECT;
import static com.nbp.util.Constants.EXPORT_ATOM;
import static com.nbp.util.Constants.EXPORT_BATCH;
import static com.nbp.util.Constants.EXPORT_BY_PROJECT;
import static com.nbp.util.Constants.EXPORT_ERRORCODE;
import static com.nbp.util.Constants.EXPORT_ERRORTYPE;
import static com.nbp.util.Constants.EXPORT_EXTAPI;
import static com.nbp.util.Constants.EXPORT_FTRANCODE;
import static com.nbp.util.Constants.EXPORT_GATEWAY_ALL;
import static com.nbp.util.Constants.EXPORT_GATEWAY_BY_PROJECT;
import static com.nbp.util.Constants.EXPORT_GWEXCEPTION;
import static com.nbp.util.Constants.EXPORT_GWRULE;
import static com.nbp.util.Constants.EXPORT_GWSTATICTBL;
import static com.nbp.util.Constants.EXPORT_GWSYSCFG;
import static com.nbp.util.Constants.EXPORT_GWTESTROUTE;
import static com.nbp.util.Constants.EXPORT_GWTRANROUTE;
import static com.nbp.util.Constants.EXPORT_HTTP;
import static com.nbp.util.Constants.EXPORT_MODULE;
import static com.nbp.util.Constants.EXPORT_P;
import static com.nbp.util.Constants.EXPORT_RULE;
import static com.nbp.util.Constants.EXPORT_SQL;
import static com.nbp.util.Constants.EXPORT_STATICTBL;
import static com.nbp.util.Constants.EXPORT_SYSCFG;
import static com.nbp.util.Constants.EXPORT_TRANCODE;
import static com.nbp.util.Constants.EXPORT_UTILBEAN;

/**
 * 导出执行器工厂类
 */
public class CheckOutFactory {

    /**
     * 全库导出
     */
    private static final Map<String, CheckOutExecutor> CHECKOUT_ALL_EXECUTOR_MAP =
            new HashMap<String, CheckOutExecutor>(4) {
                {
                    put(EXPORT_ALL, new CheckOutAll());
                    put(EXPORT_APP_ALL, new CheckOutAppAll());
                    put(EXPORT_GATEWAY_ALL, new CheckOutGwAll());
                }
            };
    /**
     * 按工程导出
     */
    private static final Map<String, CheckOutExecutor> CHECKOUT_PROJECT_EXECUTOR_MAP =
            new HashMap<String, CheckOutExecutor>(4) {
                {
                    put(EXPORT_BY_PROJECT, new CheckOutByProject());
                    put(EXPORT_APP_BY_PROJECT, new CheckOutAppByProject());
                    put(EXPORT_GATEWAY_BY_PROJECT, new CheckOutGwByProject());
                }
            };
    /**
     * 单项导出，4个参数
     */
    private static final Map<String, CheckOutExecutor> CHECKOUT_SIGN_EXECUTOR_MAP_1 =
            new HashMap<String, CheckOutExecutor>(20) {
                {
                    put(EXPORT_ATOM, new CheckOutAtom());
                    put(EXPORT_ERRORCODE, new CheckOutErrorCode());
                    put(EXPORT_ERRORTYPE, new CheckOutErrorType());
                    put(EXPORT_HTTP, new CheckOutHttpConf());
                    put(EXPORT_BATCH, new CheckOutBatch());
                    put(EXPORT_RULE, new CheckOutRuleGroup());
                    put(EXPORT_SYSCFG, new CheckOutSysCfg());
                    put(EXPORT_P, new CheckOutProject());
                    put(EXPORT_MODULE, new CheckOutModule());
                    put(EXPORT_SQL, new CheckOutSqlTemplate());
                    put(EXPORT_STATICTBL, new CheckOutStatictbl());
                    put(EXPORT_UTILBEAN, new CheckOutUtilBean());
                    put(EXPORT_GWSTATICTBL, new CheckOutGwStatictbl());
                    put(EXPORT_GWSYSCFG, new CheckOutGwSysCfg());
                    put(EXPORT_GWRULE, new CheckOutGwRuleGroup());
                }
            };
    /**
     * 单项导出，5个参数
     */
    private static final Map<String, CheckOutExecutor> CHECKOUT_SIGN_EXECUTOR_MAP_2 =
            new HashMap<String, CheckOutExecutor>(9) {
                {
                    put(EXPORT_TRANCODE, new CheckOutTran());
                    put(EXPORT_FTRANCODE, new CheckOutChnl());
                    put(EXPORT_EXTAPI, new CheckOutExtApi());
                    put(EXPORT_GWTESTROUTE, new CheckOutGwTestRoute());
                    put(EXPORT_GWTRANROUTE, new CheckOutGwTranRoute());
                    put(EXPORT_GWEXCEPTION, new CheckOutGwException());
                }
            };

    /**
     * 根据输入参数创建导出执行器
     * @param args 输入参数
     * @return 执行器
     */
    public static CheckOutExecutor createCheckout(String [] args) {
        CheckOutExecutor checkOut = null;
        switch (args.length) {
            // 入参为2个参数，获取全库导出执行器
            case 2:
                checkOut = CHECKOUT_ALL_EXECUTOR_MAP.get(args[0]);
                break;
            // 入参为3个参数，获取工程导出执行器
            case 3:
                checkOut = CHECKOUT_PROJECT_EXECUTOR_MAP.get(args[0]);
                break;
            // 入参为4个参数，获取单项导出执行器
            case 4:
                checkOut = CHECKOUT_SIGN_EXECUTOR_MAP_1.get(args[1]);
                break;
            // 入参为5个参数，获取单项导出执行器
            case 5:
                checkOut = CHECKOUT_SIGN_EXECUTOR_MAP_2.get(args[1]);
                break;
        }
        return checkOut;
    }

}

package com.nantian.nbp.cache.server.api;

import com.nantian.nbp.base.model.Api;
import com.nantian.nbp.base.model.FTranKey;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.HttpConf;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.base.model.UtilsBean;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.model.SqlTemplateStatement;
import com.nantian.nbp.flowengine.model.FlowContainer;
import com.nantian.nbp.rule.RuleDataService;

import java.util.Map;

/**
 * 缓存使用API
 * @author pengw at Jul 26, 2020 12:07:37 AM
 * @version V2.0
 */
public interface CacheClientApi extends RuleDataService {

    /**
     * 根据外部交易码和渠道号 获取对应内部流程
     * @param ftk 外部交易
     * @return 内外交易映射
     */
    TranCodeConv getTranCodeConv(FTranKey ftk);

    /**
     * 根据交易码获取交易信息
     * @param trancode 内部交易码，格式为:业务种类_内部交易码
     * @return 内部交易信息
     */
    TranCode getTranCodeMsg(String trancode);

    /**
     * 根据配置ID获取HTTP通信配置信息
     * @param serName 配置ID,通讯服务名
     * @return HTTP通信配置信息
     */
    HttpConf getHttpConf(String serName);

    /**
     * 根据定义名称，获取对应值
     * @param defName 定义名称
     * @return 对应值
     */
    String getSysCfg(String defName);

    /**
     * 根据表名获取表信息
     *
     * @param tableKey 表名
     * @param cacheKey
     * @return 表信息
     */
    Map<String, Object> getStaticTable(String tableKey, String cacheKey);

    /**
     * 根据busiType+tranCode获取决策表信息
     * @param keyName 主键：busiType+trancode
     * @return List<Map < String, Object>>
     */
    Map<String, Object> getDecisionTable(String keyName);

    /**
     * 根据流程ID获取流程执行对象
     * @param fk 流程ID,业务种类+内部交易码
     * @return 流程执行对象
     */
    FlowContainer getFlowContainer(FlowKey fk);

    /**
     * 根据UTILSBEAN_KEY获取utilsbean工具类表信息
     * @return 工具类Bean名称
     */
    PbResourceCache<UtilsBean> getUtilsBeans();

    /**
     * 根据UTILSBEAN_KEY获取utilsbean工具类表信息
     * @param beanName 主键：工具类名称
     * @return 工具类Bean名称
     */
    Api getApi(String beanName);

    SqlTemplateStatement getSqlTemplateStatement(String sqlName);
}

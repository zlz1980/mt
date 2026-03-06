/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.impl;

import com.nantian.nbp.base.model.Api;
import com.nantian.nbp.base.model.FTranKey;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.HttpConf;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.base.model.UtilsBean;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.cache.server.api.Constants;
import com.nantian.nbp.cache.server.cache.PbCache;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.model.SqlTemplateStatement;
import com.nantian.nbp.flowengine.model.FlowContainer;
import com.nantian.nbp.rule.entity.RuleBizType;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.nantian.nbp.cache.server.api.Constants.DECISION_TBL_PRE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_CONTAINER;
import static com.nantian.nbp.cache.server.api.Constants.HTTP_CONF_KEY;
import static com.nantian.nbp.cache.server.api.Constants.RULE_BIZ_TYPE_KEY;
import static com.nantian.nbp.cache.server.api.Constants.SQL_TEMPLATE_KEY;
import static com.nantian.nbp.cache.server.api.Constants.STATIC_TBL_PRE;
import static com.nantian.nbp.cache.server.api.Constants.SYS_CFG_KEY;
import static com.nantian.nbp.cache.server.api.Constants.TRAN_CODE_CONV_KEY;
import static com.nantian.nbp.cache.server.api.Constants.TRAN_CODE_MSG_KEY;
import static com.nantian.nbp.cache.server.api.Constants.UTILS_BEAN_KEY;

/**
* 缓存使用实现
* @version V2.0
*/
public class CacheClientApiImpl implements CacheClientApi {

	private final Supplier<PbCache> pbCacheSupplier;

	public CacheClientApiImpl(Supplier<PbCache> pbCacheSupplier) {
        this.pbCacheSupplier = pbCacheSupplier;
	}

	@Override
	public TranCodeConv getTranCodeConv(FTranKey ftk) {
		return cache().get(TRAN_CODE_CONV_KEY,ftk.toString());
	}

	@Override
	public TranCode getTranCodeMsg(String tranCode) {
		return cache().get(TRAN_CODE_MSG_KEY,tranCode);
	}

	@Override
	public HttpConf getHttpConf(String serName) {
		return cache().get(HTTP_CONF_KEY,serName);
	}

	@Override
	public String getSysCfg(String defName){
		return cache().get(SYS_CFG_KEY ,defName);
	}

	@Override
	public Map<String, Object> getStaticTable(String tableKey, String cacheKey) {
		Map<String, Object> sqlCacheData = cache().get(STATIC_TBL_PRE , tableKey);
		if(Objects.isNull(sqlCacheData)) {
			return null;
		}
		return (Map<String, Object>)sqlCacheData.get(cacheKey);
	}

	@Override
	public Map<String, Object> getDecisionTable(String keyName) {
		return cache().get(DECISION_TBL_PRE,keyName);
	}

	@Override
	public FlowContainer getFlowContainer(FlowKey flowKey) {
		return cache().get(FLOW_CONTAINER,flowKey.toString());
	}

	@Override
	public PbResourceCache<UtilsBean> getUtilsBeans() {
		return (PbResourceCache<UtilsBean>) cache().get(UTILS_BEAN_KEY);
	}

	@Override
	public Api getApi(String beanName) {
		return cache().get(Constants.API_KEY,beanName);
	}

	@Override
	public SqlTemplateStatement getSqlTemplateStatement(String sqlName) {
		return cache().get(SQL_TEMPLATE_KEY,sqlName);
	}

	@Override
	public RuleBizType getRuleBizType(String bizType) {
		return cache().get(RULE_BIZ_TYPE_KEY, bizType);
	}

	private PbCache cache(){
		return pbCacheSupplier.get();
	}
}

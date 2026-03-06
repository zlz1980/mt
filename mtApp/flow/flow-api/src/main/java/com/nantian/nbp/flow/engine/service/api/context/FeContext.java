package com.nantian.nbp.flow.engine.service.api.context;

import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.base.model.SagaTransitionManager;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.base.model.UtilsBean;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.ev.PbElExpressionParser;
import com.nantian.nbp.ev.PbEvContext;
import com.nantian.nbp.ev.PbVal;
import com.nantian.nbp.flow.engine.service.api.Constants;
import com.nantian.nbp.flow.engine.service.api.TranContext;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.FeConstants.CTX_FLAG;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.IN_HEADER_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.IN_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.MAP_INIT;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.OUT_HEADER_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.PB_LOG;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.SYS_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.TMP_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.TRAN_SCOPE;

/**
 * 流程引擎引擎上下文
 *
 * @author pengw at Jul 26, 2020 12:07:37 AM
 * @version V2.0
 */
public class FeContext implements TranContext {
    /**
     * 输入作用域
     */
    private final PbScope<Object> inMap;
    /**
     * 输入头作用域
     */
    private final PbHeader inHeaderMap;
    /**
     * 交易作用域
     */
    private final PbScope<Object> tranMap;
    /**
     * 输出头作用域
     */
    private final PbHeader outHeaderMap = new PbHeader(MAP_INIT);
    /**
     * 系统作用域
     */
    private final PbScope<Object> sysMap;
    /**
     * 临时作用域，需临时变量相关原子交易操作
     */
    private final PbScope<Object> tmpMap = new PbScope<>(MAP_INIT);
    /**
     * 流程上下文属性
     */
    private final PbScope<Object> props;
    /**
     * 表达式上下文
     */
    private final PbEvContext evCtx = new PbEvContext();
    /**
     * 事物管理器
     */
    private SagaTransitionManager sagaTransitionManager;
    /**
     * 内部交易信息
     */
    private TranCode tranCodeObj = null;
    /**
     * 外部交易码
     */
    private String feTranCode = null;
    /**
     * 内部交易码
     */
    private String tranCode = null;
    /**
     * 业务种类
     */
    private String bizType = null;
    /**
     * 缓存接口
     */
    private final CacheClientApi cacheClientApi;
    /**
     * 输出作用域
     */
    private PbScope<Object> outMap;

    public FeContext(CacheClientApi cacheClientApi) {
        this(new PbScope<>(MAP_INIT), new PbScope<>(MAP_INIT), new PbHeader(MAP_INIT), new PbScope<>(MAP_INIT),cacheClientApi);
    }

    public FeContext(PbScope<Object> sysMap, PbScope<Object> tranMap, PbScope<Object> inMap,CacheClientApi cacheClientApi) {
        this(sysMap, tranMap, new PbHeader(MAP_INIT), inMap,cacheClientApi);
    }

    public FeContext(PbScope<Object> sysMap, PbScope<Object> tranMap, PbHeader inHeaderMap, PbScope<Object> inMap,CacheClientApi cacheClientApi) {
        props = new PbScope<>(MAP_INIT);
        this.inHeaderMap = inHeaderMap;
        props.put(IN_HEADER_SCOPE, this.inHeaderMap);

        this.inMap = inMap;
        props.put(IN_SCOPE, inMap);

        props.put(OUT_HEADER_SCOPE, this.outHeaderMap);

        this.tranMap = tranMap;
        props.put(TRAN_SCOPE, this.tranMap);

        this.sysMap = sysMap;
        props.put(SYS_SCOPE, this.sysMap);

        props.put(TMP_SCOPE, tmpMap);

        evCtx.setVariable(CTX_FLAG, props);

        //获取上下文bean对象
        this.cacheClientApi= cacheClientApi;
        setUtilsBean(cacheClientApi);
    }

    @Override
    public PbHeader getInHeaderScope() {
        return inHeaderMap;
    }

    @Override
    public PbScope<Object> getTmpScope() {
        return tmpMap;
    }

    public PbHeader getOutHeaderScope() {
        return outHeaderMap;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getFeTranCode() {
        return feTranCode;
    }

    @Override
    public PbLog getPbLog() {
        return (PbLog)getSysScope().get(PB_LOG);
    }

    public void setFeTranCode(String feTranCode) {
        this.feTranCode = feTranCode;
    }

    public PbScope<Object> getProps() {
        return props;
    }

    public CacheClientApi getCacheClientApi() {
        return cacheClientApi;
    }

    public TranCode getTranCodeObj() {
        return tranCodeObj;
    }

    public void setTranCodeObj(TranCode tranCodeObj) {
        this.tranCodeObj = tranCodeObj;
    }

    public PbScope<Object> getOutScope() {
        return outMap;
    }

    public void setOutScope(PbScope<Object> outMap) {
        this.outMap = outMap;
    }


    public PbEvContext getEvCtx() {
        return evCtx;
    }

    @Override
    public PbScope<Object> getInScope() {
        return inMap;
    }

    @Override
    public PbScope<Object> getScopeVal(String key){
        return (PbScope<Object>)getValByMainKey(key);
    }

    private Object getValByMainKey(String key){
        return PbElExpressionParser.getVal(Constants.EL_CTX_FLAG+"["+key+"]",evCtx)
                .getVal();
    }

    @Override
    public PbScope<Object> getSysScope() {
        return sysMap;
    }

    @Override
    public PbScope<Object> getTranScope() {
        return tranMap;
    }

    /**
     * 对${}标注的变量取值
     * @param key ${}的变量
     * @return 值
     */
    @Override
    public PbVal getCtxVal(String key) {
        String exp = MapUtils.repElVal(key);
        return PbElExpressionParser.getVal(exp,evCtx);
    }

    /**
     * 对${}标注的变量取布尔值
     * @param key ${}的变量
     * @return 布尔值
     */
    @Override
    public Boolean getCtxBoolVal(String key) {
        String exp = MapUtils.repElVal(key);
        return PbElExpressionParser.getBoolVal(exp,evCtx);
    }

    /**
     * 判断参数是否包含${},包含从作用域取值，不包含直接按常量返回
     * @param key ${}变量或常量名
     * @return 值
     */
    @Override
    public Object getValue(String key) {
        if(MapUtils.checkEl(key)){
            return getCtxVal(key).getVal();
        }
        return key;
    }

    public SagaTransitionManager getSagaTransitionManager() {
        return sagaTransitionManager;
    }

    public void setSagaTransitionManager(SagaTransitionManager sagaTransitionManager) {
        this.sagaTransitionManager = sagaTransitionManager;
    }

    private void setUtilsBean(CacheClientApi cacheClientApi) {
        if(Objects.isNull(cacheClientApi)){
            return;
        }
        PbResourceCache<UtilsBean> utilsBeanSet = cacheClientApi.getUtilsBeans();
        if(Objects.nonNull(utilsBeanSet)){
            for (UtilsBean utilsBean : utilsBeanSet.values()){
                if(CTX_FLAG.equals(utilsBean.getBeanName())) {
                    throw new RuntimeException("bean name can not be 'ctx'");
                }
                evCtx.setVariable(utilsBean.getBeanName(), utilsBean.getBean());
            }
        }
    }

    /**
     * 输出el表达式中变量对应的值
     * @param key el表达式
     * @return 变量与值的对应关系
     */
    public String printElVals(String key){
        StringBuilder keySb = new StringBuilder();
        for (String m:MapUtils.getElKeys(key)) {
            keySb.append(m);
            keySb.append(":");
            if (null == getCtxVal(m) || null == getCtxVal(m).getVal()) {
                keySb.append("null");
            } else {
                keySb.append(getCtxVal(m).getVal().toString());
            }
            keySb.append(" | ");
        }
        return keySb.toString();
    }
}

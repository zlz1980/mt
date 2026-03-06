package com.nantian.nbp.log4j2;

import com.nantian.nbp.base.model.FTranKey;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.utils.PbEntity;
import com.nantian.nbp.utils.PbThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.nantian.nbp.log4j2.TranLogConstants.TRACE_GID;
import static com.nantian.nbp.log4j2.TranLogConstants.TRACE_LID;
import static com.nantian.nbp.log4j2.TranLogConstants.TRACE_PID;
import static com.nantian.nbp.log4j2.TranLogConstants.LOG_CHNL_NO;
import static com.nantian.nbp.log4j2.TranLogConstants.LOG_KEY;
import static com.nantian.nbp.log4j2.TranLogConstants.LOG_TRAN_CODE;
import static com.nantian.nbp.log4j2.TranLogConstants.LOG_TYPE;
import static com.nantian.nbp.log4j2.TranLogConstants.POIN_PB_LEVEL;
import static com.nantian.nbp.log4j2.TranLogConstants.TRAN_TYPE;
import static com.nantian.nbp.log4j2.TranLogConstants.UUID;

public class ThreadContextRouting {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadContextRouting.class);
    /**
     * POIN健康检查URL
     */
    private final static String POIN_HEALTH = "/poin/health";
    /**
     * POIN数据库健康检查URL
     */
    private final static String POIN_DB_HEALTH = "/webapi/poin/db/health";
    /**
     * POIN edsp健康检查URL
     */
    private final static String POIN_EDSP_HEALTH = "/poin/edspHealth";
    private final CacheClientApi cacheClientApi;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public ThreadContextRouting(CacheClientApi cacheClientApi) {
        this.cacheClientApi = cacheClientApi;
    }

    public void tranLogRoute(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // POIN健康检查请求，不作处理，继续执行后续过滤器链
        if (antPathMatcher.match(POIN_HEALTH, uri) || antPathMatcher.match(POIN_DB_HEALTH, uri) || antPathMatcher.match(POIN_EDSP_HEALTH, uri)) {
            return;
        }
        if (antPathMatcher.match("/tran/*/*", uri)) {
            PbEntity entity = new PbEntity();
            PbThreadLocal.set(entity);
            String[] fTran = uri.split("/", -1);
            FTranKey ftk = new FTranKey(fTran[2], fTran[3]);
            TranCodeConv tcc = cacheClientApi.getTranCodeConv(ftk);
            mdcClear();
            MDC.put(LOG_TYPE, TRAN_TYPE);
            if (!ObjectUtils.isEmpty(tcc)) {
                MDC.put(UUID, entity.getBizId());
                MDC.put(LOG_KEY, TRAN_TYPE + ftk);
                MDC.put(LOG_CHNL_NO, ftk.getChnlNo());
                MDC.put(LOG_TRAN_CODE, ftk.getfTranCode());
                MDC.put(POIN_PB_LEVEL, tcc.getLogLevel());

                MDC.put(TRACE_GID, request.getHeader(TRACE_GID));
                MDC.put(TRACE_PID, request.getHeader(TRACE_PID));
                MDC.put(TRACE_LID, request.getHeader(TRACE_LID));
            } else {
                //交易码匹配不上，走默认日志输出，防止日志丢失
                MDC.put(UUID, entity.getBizId());
                MDC.put(LOG_KEY, "default");
            }
        }
    }

    public void tranLogRoute(String uri, Map<String, List<String>> header) {
        // POIN健康检查请求，不作处理，继续执行后续过滤器链
        if (antPathMatcher.match(POIN_HEALTH, uri) || antPathMatcher.match(POIN_DB_HEALTH, uri) || antPathMatcher.match(POIN_EDSP_HEALTH, uri)) {
            return;
        }
        if (antPathMatcher.match("/tran/*/*", uri)) {
            PbEntity entity = new PbEntity();
            PbThreadLocal.set(entity);
            String[] fTran = uri.split("/", -1);
            FTranKey ftk = new FTranKey(fTran[2], fTran[3]);
            TranCodeConv tcc = cacheClientApi.getTranCodeConv(ftk);
            if (!ObjectUtils.isEmpty(tcc)) {
                /* 消除上笔影响 */
                mdcClear();
                MDC.put(LOG_TYPE, TRAN_TYPE);
                MDC.put(UUID, entity.getBizId());
                MDC.put(LOG_KEY, TRAN_TYPE + ftk);
                MDC.put(LOG_CHNL_NO, ftk.getChnlNo());
                MDC.put(LOG_TRAN_CODE, ftk.getfTranCode());
                MDC.put(POIN_PB_LEVEL, tcc.getLogLevel());
                List<String> gidList = header.get(TRACE_GID);
                if (!CollectionUtils.isEmpty(gidList)) {
                    MDC.put(TRACE_GID, gidList.get(0));
                }
                List<String> lidList = header.get(TRACE_LID);
                if (!CollectionUtils.isEmpty(lidList)) {
                    MDC.put(TRACE_LID, lidList.get(0));
                }
                List<String> pidList = header.get(TRACE_PID);
                if (!CollectionUtils.isEmpty(pidList)) {
                    MDC.put(TRACE_PID, pidList.get(0));
                }
            } else {
                //交易码匹配不上，走默认日志输出，防止日志丢失
                /* 消除上笔影响 */
                mdcClear();
                MDC.put(LOG_TYPE, TRAN_TYPE);
                MDC.put(UUID, entity.getBizId());
                MDC.put(LOG_KEY, "default");
            }
        }
    }

    public void afterClear() {
        try {
            PbThreadLocal.clear();
        } catch (Exception e) {
            LOGGER.error("PbThreadLocal clear failed", e);
        }
        mdcClear();
    }

    private void mdcClear() {
        try {
            MDC.remove(LOG_TYPE);
            MDC.remove(UUID);
            MDC.remove(LOG_KEY);
            MDC.remove(LOG_CHNL_NO);
            MDC.remove(LOG_TRAN_CODE);
            MDC.remove(POIN_PB_LEVEL);
            MDC.remove(TRACE_GID);
            MDC.remove(TRACE_LID);
            MDC.remove(TRACE_PID);
        } catch (Exception e) {
            LOGGER.error("MDC clear failed", e);
        }
    }

}

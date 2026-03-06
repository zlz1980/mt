package com.nantian.nbp.main.controller;

import com.nantian.nbp.cache.server.CacheLoader;
import com.nantian.nbp.cache.server.model.RefushResult;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;
import static com.nantian.nbp.cache.server.api.Constants.LOG_TYPE_CACHE;


@RestController
public class CacheController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LOG_TYPE_CACHE);

    private final CacheLoader slaveCacheLoader;
    /**
     * 初始化状态
     */
    private final AtomicBoolean initFlag = new AtomicBoolean(Boolean.FALSE);

    /**
     * 本服务名
     */
    @Value("${spring.application.name}")
    private String serverName;

    public CacheController(CacheLoader slaveCacheLoader) {
        this.slaveCacheLoader = slaveCacheLoader;
    }

    @RequestMapping(value = "/initAllCache", method = {RequestMethod.GET})
    public RefushResult initAllCacheData(String token) {
        // 计时器，用于记录加载耗时
        Long startTime = Timer.getStartTime();
        // 记录开始重新加载所有缓存的日志
        LOGGER.info("===================Reload all cache Begin [{}]===================", serverName);
        RefushResult refushResult = new RefushResult();
        if (!initFlag.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            refushResult.setResultFlag(false);
            refushResult.setErrorMsg("All cache is initializing");
            return refushResult;
        }
        try {
            //先刷从缓存
            refushResult = slaveCacheLoader.reloadAll(serverName);
            //如果刷新失败，报错，停止刷新
            if (!refushResult.isResultFlag()) {
                //刷新缓存失败-返回刷新缓存失败
                return refushResult;
            }
            return refushResult;
        } catch (Exception e) {
            // 处理刷新缓存时的异常
            LOGGER.error(APP_CACHE_ERR_KEY + "Failed to reload all cache, errorInfo: [{}]", e.getMessage(), e);
            refushResult.setResultFlag(false);
            refushResult.setErrorMsg("Failed to reload all cache,errorInfo[" + e.getMessage() + "]");
            return refushResult;
        } finally {
            initFlag.set(Boolean.FALSE);
            LOGGER.info("===================Reload all cache End [{}],usedTime[{}]ms ===================", serverName, Timer.getUsedTime(startTime));
        }
    }
}

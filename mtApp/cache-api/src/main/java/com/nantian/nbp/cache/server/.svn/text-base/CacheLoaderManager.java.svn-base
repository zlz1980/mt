/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.cache.server;

import com.nantian.nbp.cache.server.cache.PbCache;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.lock.ReadWriteCacheLock;
import com.nantian.nbp.cache.server.model.RefushResult;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;

public class CacheLoaderManager implements CacheLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheLoaderManager.class);
    private final SqlSessionTemplate sqlSessionTemplate;
    private final List<ResourceCacheLoader<?>> baseResLoaderList = new LinkedList<>();
    private final List<ResourceCacheLoader<?>> extResLoaderList = new LinkedList<>();
    private final long initCacheTimeout;
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    private PbCache mainPbCache;
    private PbCache slavePbCache;
    private PbCache flushingPbCache;


    public CacheLoaderManager(SqlSessionTemplate sqlSessionTemplate, long initCacheTimeout) {
        mainPbCache = new PbCache("mainCache", 20);
        slavePbCache = new PbCache("slaveCache", 20);
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.initCacheTimeout = initCacheTimeout;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Override
    public PbCache getMainPbCache() {
        ReadWriteCacheLock.readLock();
        try {
            return mainPbCache;
        } finally {
            ReadWriteCacheLock.readUnlock();
        }
    }

    @Override
    public PbCache getFlushingPbCache() {
        return flushingPbCache;
    }

    @Override
    public void registerBaseResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader) {
        baseResLoaderList.add(resourceCacheLoader);
    }

    @Override
    public void registerExtResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader) {
        extResLoaderList.add(resourceCacheLoader);
    }

    /**
     * 初始化所有缓存
     * 此方法负责初始化系统中所有相关的缓存，确保在程序启动或特定操作时，所有缓存被正确加载和刷新
     *
     * @param serverName 服务器名称，用于标识执行初始化操作的服务器
     */
    @Override
    public void initAll(String serverName) {
        // 定义一个程序关闭策略，当需要紧急关闭程序时，立即关闭所有执行器服务并退出程序
        Consumer<ExecutorService> programShutdown = (executorService) -> {
            executorService.shutdownNow();
            System.exit(1);
        };
        // 将主缓存对象赋值给flushingPbCache，以便在后续操作中使用
        flushingPbCache = mainPbCache;
        // 加载缓存资源，包括基础资源和扩展资源，使用指定的执行器服务和关闭策略
        loadCacheResource(serverName, mainPbCache, executorService, programShutdown);
    }

    /**
     * 重新加载所有缓存
     * 此方法首先将刷新操作记录到日志中，然后尝试重新加载缓存资源
     * 如果重新加载成功，则将主缓存切换到新的缓存上，并清除旧的缓存
     *
     * @param serverName 服务器名称，用于标识哪个服务器的缓存需要重新加载
     * @return 返回重新加载的结果，包括是否成功等信息
     */
    @Override
    public RefushResult reloadAll(String serverName) {
        // 准备切换缓存，将从库缓存设置为正在刷新的缓存
        flushingPbCache = slavePbCache;
        // 加载缓存资源，并获取刷新结果
        RefushResult refushResult = loadCacheResource(serverName, slavePbCache, executorService, null);
        // 如果刷新结果标志为成功，则进行缓存切换
        if (refushResult.isResultFlag()) {
            // 保存当前主缓存作为历史缓存
            PbCache hisCache = mainPbCache;
            // 获取写锁以进行缓存切换操作
            ReadWriteCacheLock.writeLock();
            try {
                // 切换主从缓存
                this.mainPbCache = slavePbCache;
                slavePbCache = hisCache;
            } finally {
                // 释放写锁
                ReadWriteCacheLock.writeUnlock();
            }
            // 清除旧的从库缓存
            slavePbCache.clear();
        }
        // 返回刷新结果
        return refushResult;
    }




    /**
     * 加载缓存资源方法
     * 该方法负责异步加载基础资源和扩展资源，并将它们存储在缓存中它使用了线程池来并行执行加载任务，
     * 并使用CountDownLatch来等待所有任务完成
     *
     * @param serverName      服务器名称，用于标识缓存所属的服务器
     * @param pbCache         缓存对象，用于存储所有加载的资源缓存
     * @param executorService 执行器服务，用于提交异步任务
     * @param consumer        消费者，用于在发生异常时执行一些操作，如关闭执行器服务
     * @return 返回刷新结果对象，包含操作是否成功和可能的错误信息
     */
    private RefushResult loadCacheResource(String serverName, PbCache pbCache, ExecutorService executorService,
            Consumer<ExecutorService> consumer) {
        // 记录开始时间
        long b = System.currentTimeMillis();
        // 初始化标志，用于标记缓存初始化是否成功
        AtomicBoolean initFlag = new AtomicBoolean(true);
        // 错误信息缓冲区
        StringBuffer errorInfo = new StringBuffer();
        // 刷新结果对象
        RefushResult refushResult = new RefushResult();
        // 清空缓存，准备进行新的缓存加载
        pbCache.clear();
        // 使用CountDownLatch来等待两个异步任务完成
        CountDownLatch downLatch = new CountDownLatch(2);

        // 初始化基础资源的Runnable任务
        Runnable initBaseResource = () -> {
            try {
                // 记录开始时间
                long a = System.currentTimeMillis();
                // 遍历基础资源加载器列表，初始化并缓存资源
                for (ResourceCacheLoader<?> resourceCacheLoader : baseResLoaderList) {
                    PbResourceCache<?> resCache = resourceCacheLoader.initCache();
                    // 如果资源缓存非空，则放入缓存中，否则记录警告日志
                    if (Objects.nonNull(resCache)) {
                        pbCache.put(resCache.getCacheName(), resCache);
                    } else {
                        LOGGER.warn("Base resource cache[{}] is null", resourceCacheLoader.getResourceName());
                    }
                }
                // 记录基础数据加载耗时
                LOGGER.info("Base data cost[{}]ms", System.currentTimeMillis() - a);
            } catch (Exception e) {
                // 记录错误日志，设置初始化标志为false，并记录错误信息
                LOGGER.error(APP_CACHE_ERR_KEY + "init cache baseResource error will shutdown", e);
                errorInfo.append(" ").append(e.getMessage());
                initFlag.set(false);
                // 如果消费者非空，则执行消费者操作
                if (consumer != null) {
                    consumer.accept(executorService);
                }
            } finally {
                // 计数减一
                downLatch.countDown();
            }
        };
        // 提交基础资源加载任务到线程池
        executorService.submit(initBaseResource);

        // 初始化所有流资源的Runnable任务
        Runnable initAllFlowResource = () -> {
            try {
                // 记录开始时间
                long l = System.currentTimeMillis();
                // 遍历扩展资源加载器列表，初始化并缓存资源
                for (ResourceCacheLoader<?> resourceCacheLoader : extResLoaderList) {
                    PbResourceCache<?> resCache = resourceCacheLoader.initCache();
                    // 如果资源缓存非空，则放入缓存中，否则记录警告日志
                    if (Objects.nonNull(resCache)) {
                        pbCache.put(resCache.getCacheName(), resCache);
                    } else {
                        LOGGER.warn("Ext resource cache[{}] is null", resourceCacheLoader.getResourceName());
                    }
                }
                // 记录流数据加载耗时
                LOGGER.info("Flow data cost[{}]ms", System.currentTimeMillis() - l);
            } catch (Exception e) {
                // 记录错误日志，设置初始化标志为false，并记录错误信息
                LOGGER.error(APP_CACHE_ERR_KEY + "init cache all flow error will shutdown", e);
                errorInfo.append(" ").append(e.getMessage());
                initFlag.set(false);
                // 如果消费者非空，则执行消费者操作
                if (consumer != null) {
                    consumer.accept(executorService);
                }
            } finally {
                // 计数减一
                downLatch.countDown();
            }
        };
        // 提交扩展资源加载任务到线程池
        executorService.submit(initAllFlowResource);

        // 等待所有任务完成或超时
        try {
            boolean res = downLatch.await(initCacheTimeout, TimeUnit.SECONDS);
            // 记录所有缓存数据加载是否完成及耗时
            LOGGER.info("All cache data[{}] usedTime[{}]ms", res, System.currentTimeMillis() - b);
        } catch (InterruptedException e) {
            // 记录错误日志，设置初始化标志为false，并记录错误信息
            LOGGER.error(APP_CACHE_ERR_KEY + "initAll failed. CountDownLatch wait time >=[{}],(cacheData.initCacheTimeout)", initCacheTimeout, e);
            initFlag.set(false);
            errorInfo.append(" ").append(e.getMessage());
            // 如果消费者非空，则执行消费者操作
            if (consumer != null) {
                consumer.accept(executorService);
            }
        }
        // 设置刷新结果的标志和错误信息，并返回
        refushResult.setResultFlag(initFlag.get());
        refushResult.setErrorMsg(errorInfo.toString());
        return refushResult;
    }


}


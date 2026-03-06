/**
 *  Copyright(c) by NANTIAN Electronic Information Co., Ltd.
 *  
 *  Project: Enterprise Distributed Service Platform (Message)
 *  
 *  @Package: com.nantian.nbp.config
 *  @author: 
 */
package com.nantian.nbp.main.config.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolManager {

	private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();

	private ThreadPoolManager() {}
	
	public static ThreadPoolManager getInstance() {
		return INSTANCE;
	}

	private final RegexHashMap<ThreadPoolTaskExecutor> threadPools = new RegexHashMap<>();

	/**
	 * 初始化线程池
	 */
	public void initThreadPool(Map<String, ThreadPool> pools) {
		if (null != pools && !pools.isEmpty()) {
			pools.forEach(this::buildTaskExecutor);
		}
		// 如果没有配置默认线程池，初始一个
        if (pools != null && null == pools.get("default")) {
            buildTaskExecutor("default", new ThreadPool());
        }
    }

	public void buildTaskExecutor(String threadPoolName, ThreadPool threadPoolConfig) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心线程数
		executor.setCorePoolSize(threadPoolConfig.getCorePoolSize());
		// 最大线程数
		executor.setMaxPoolSize(threadPoolConfig.getMaxPoolSize());
		// 任务队列大小
		executor.setQueueCapacity(threadPoolConfig.getQueueCapacity());
		// 线程前缀名
		executor.setThreadNamePrefix(threadPoolConfig.getThreadNamePrefix());
		// 线程的空闲时间
		executor.setKeepAliveSeconds(threadPoolConfig.getKeepAliveSeconds());
		// 拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		// 线程初始化
		executor.initialize();

		threadPools.put(threadPoolName, executor);
	}

	/**
	 * 获取指定线程池
	 */
	public ThreadPoolTaskExecutor getThreadPool(String threadPoolName) {
		return threadPools.get(threadPoolName);
	}

	/**
	 * 提交线程任务
	 */
	public Future<?> submit(String threadPoolName, Runnable task){
		ThreadPoolTaskExecutor threadPool =  threadPools.get(threadPoolName);
		if (null == threadPool) {
			threadPool = threadPools.get("default");
		}
		return threadPool.submit(task);
	}
}

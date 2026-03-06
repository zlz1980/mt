/**
 *  Copyright(c) by NANTIAN Electronic Information Co., Ltd.
 *  
 *  Project: Enterprise Distributed Service Platform (Message)
 *  
 *  @Package: com.nantian.nbp.config
 *  @author: 
 */
package com.nantian.nbp.main.config.thread;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池初始化
 * @author Administrator
 */
@Configuration
@ConfigurationProperties(prefix = "async")
@ConditionalOnProperty(prefix = "async", name = "enable", havingValue = "true",matchIfMissing = true)
public class ThreadPoolConfiguration {
	
	private Map<String,ThreadPool> pools = new ConcurrentHashMap<>();

	public Map<String, ThreadPool> getPools() {
		return pools;
	}

	public void setPools(Map<String, ThreadPool> pools) {
		this.pools = pools;
	}

	@PostConstruct
	public void init() {
		ThreadPoolManager.getInstance().initThreadPool(pools);
	}
	
}

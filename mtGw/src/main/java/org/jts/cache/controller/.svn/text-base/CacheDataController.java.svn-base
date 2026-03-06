package org.jts.cache.controller;

import org.jts.cache.CacheClientApi;
import org.jts.cache.loader.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@RestController
public class CacheDataController {

	private static final Logger logger = LoggerFactory.getLogger(CacheDataController.class);

	private final CacheLoader slaveCacheLoader;

	private final CacheClientApi cacheClientApi;
	/** 本服务名 */
	@Value("${spring.application.name:mtgw}")
	private String serverName;
	/** 初始化状态 */
    private final AtomicBoolean initFlag = new AtomicBoolean(Boolean.FALSE);

    public CacheDataController(CacheLoader slaveCacheLoader, CacheClientApi cacheClientApi) {
        this.slaveCacheLoader = slaveCacheLoader;
        this.cacheClientApi = cacheClientApi;
    }

    @RequestMapping(value = "/initAllCache", method = { RequestMethod.GET })
	public String initAllCacheData() {
		if(!initFlag.compareAndSet(Boolean.FALSE, Boolean.TRUE)){
			return "Initializing";
		}
		try {
			//先刷从缓存
			boolean isSuccess = slaveCacheLoader.reloadAll(serverName);
			//如果刷新失败，报错，停止刷新
			if(!isSuccess){
				//刷新缓存失败-返回刷新缓存失败
				return "FAILED";
			}
			return "SUCCESS";
		} finally {
			initFlag.set(Boolean.FALSE);
		}
	}

	@RequestMapping(value = "/getAllCache", method = { RequestMethod.GET })
	public String getAllCache() {
		List<String> list = cacheClientApi.getAllNewCodeList();
		return Arrays.toString(list.toArray(new String[0]));
	}
}

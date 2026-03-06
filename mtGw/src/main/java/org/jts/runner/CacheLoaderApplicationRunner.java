package org.jts.runner;

import org.jts.cache.loader.CacheLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CacheLoaderApplicationRunner implements ApplicationRunner {

    /** 本服务名 */
    @Value("${spring.application.name:mtgw}")
    private String serverName;

    private final CacheLoader cacheLoader;

    public CacheLoaderApplicationRunner(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public void run(ApplicationArguments args) {
        cacheLoader.initAll(serverName);
    }
}

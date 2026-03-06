package com.nantian.nbp.log4j2.web;

import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.log4j2.ThreadContextRouting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PbLogConfig {

    @Bean
    public ThreadContextRouting threadContextRouting(CacheClientApi cacheClientApi){
        return new ThreadContextRouting(cacheClientApi);
    }

    @Bean
    public PbLogHandlerInterceptor pbLogHandlerInterceptor(ThreadContextRouting threadContextRouting){
        return new PbLogHandlerInterceptor(threadContextRouting);
    }

}

package org.jts.config;

import org.jts.cache.CacheClientApi;
import org.jts.filter.AuthGatewayFilterFactory;
import org.jts.filter.ReqBlockGatewayFilterFactory;
import org.jts.predicate.SvcDispatcherRoutePredicateFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GwConfig {
    @Bean
    public SvcDispatcherRoutePredicateFactory bpsSvcDispatcherRoutePredicateFactory(CacheClientApi cacheClientApi,
                                                                                    SqlSessionTemplate sqlSessionTemplate) {
        return new SvcDispatcherRoutePredicateFactory(cacheClientApi,sqlSessionTemplate);
    }
    @Bean
    public ReqBlockGatewayFilterFactory reqBlockGatewayFilterFactory(){
        return new ReqBlockGatewayFilterFactory();
    }

    @Bean
    public AuthGatewayFilterFactory authGatewayFilterFactory(){
        return new AuthGatewayFilterFactory();
    }

}

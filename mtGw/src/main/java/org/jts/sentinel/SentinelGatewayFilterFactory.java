package org.jts.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

public class SentinelGatewayFilterFactory extends AbstractGatewayFilterFactory<SentinelGatewayFilterFactory.Config> {

    public SentinelGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new SentinelGatewayFilter();
    }

    public static class Config {

    }
}

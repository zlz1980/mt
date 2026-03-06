package com.nantian.nbp.main.config;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Configuration
public class PbRestTemplateConfig {
    @Value("${restTemplate.connectTimeout:6000}")
    private int connectTimeout;

    @Value("${restTemplate.readTimeout:60000}")
    private int readTimeout;

    @Value("${restTemplate.requestTimeout:6000}")
    private int requestTimeout;

    @Value("${restTemplate.maxIdleTime:6000}")
    private int maxIdleTime;

    @Value("${restTemplate.maxTotal:200}")
    private int maxTotal;

    @Value("${restTemplate.defaultMaxPerRoute:20}")
    private int defaultMaxPerRoute;

    @Value("${restTemplate.validateAfterInactivity:30000}")
    private int validateAfterInactivity;

    @Bean(value="restTemplate4Svc")
    // @LoadBalanced
    public RestTemplate restTemplate4Svc(RestTemplateBuilder builder){
        RestTemplate restTemplate = builder.build();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }

    @Bean(value="restTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        RestTemplate restTemplate = builder.build();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClientBuilder().build());
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(requestTimeout);
        return clientHttpRequestFactory;
    }

    private HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingConnectionManager());
        httpClientBuilder.evictExpiredConnections();
        httpClientBuilder.evictIdleConnections(maxIdleTime, TimeUnit.MILLISECONDS);
        return httpClientBuilder;
    }

    private HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(maxTotal);
        poolingConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        poolingConnectionManager.setValidateAfterInactivity(validateAfterInactivity);

        return poolingConnectionManager;
    }
}

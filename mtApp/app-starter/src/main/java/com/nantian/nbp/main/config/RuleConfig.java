package com.nantian.nbp.main.config;

import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.rule.RuleDataService;
import com.nantian.nbp.rule.RuleService;
import com.nantian.nbp.rule.impl.RuleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 事务配置
 * @author JiangTaiSheng
 */
@Configuration
public class RuleConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfig.class);

    @Bean
    public RuleService ruleService(CacheClientApi cacheClientApi){
        return new RuleServiceImpl(cacheClientApi);
    }

}
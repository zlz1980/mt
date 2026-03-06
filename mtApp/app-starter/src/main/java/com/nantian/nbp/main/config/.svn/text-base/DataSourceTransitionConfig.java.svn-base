package com.nantian.nbp.main.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


/**
 * 事务配置
 *
 * @author JiangTaiSheng
 */
@Configuration
public class DataSourceTransitionConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceTransitionConfig.class);

    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
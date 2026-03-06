package com.nantian.nbp.main.config;

import com.nantian.nbp.cache.server.init.utils.MapperUtils;
import com.nantian.nbp.mybatis.service.api.bz.FbspSqlTemplateService;
import com.nantian.nbp.mybatis.service.bz.impl.FbspSqlTemplateServiceImpl;
import com.nantian.nbp.rule.cache.server.init.util.RuleMapperUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置ObtainSqlSessionFactory
 */
@Configuration
@MapperScan({"com.nantian.**.mapper","com.n.**.mapper"})
public class SqlSessionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSessionConfig.class);

    @Bean(name = "fbspSqlTemplateService")
    public FbspSqlTemplateService getFbspSqlTemplateService(SqlSessionTemplate sqlSessionTemplate) {
        MapperUtils.addMappers(sqlSessionTemplate.getConfiguration());
        RuleMapperUtils.addMappers(sqlSessionTemplate.getConfiguration());
        return new FbspSqlTemplateServiceImpl(sqlSessionTemplate.getConfiguration());
    }
}

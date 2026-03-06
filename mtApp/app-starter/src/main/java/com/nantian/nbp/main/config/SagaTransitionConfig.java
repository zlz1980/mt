package com.nantian.nbp.main.config;

import com.nantian.nbp.cache.server.init.mapper.saga.InitSagaMapper;
import com.nantian.nbp.flow.engine.service.api.saga.DefSagaTransitionResConfirmationImpl;
import com.nantian.nbp.flow.engine.service.api.saga.SagaTransitionResConfirmation;
import com.nantian.nbp.main.config.thread.ThreadPool;
import com.nantian.nbp.main.config.thread.ThreadPoolManager;
import com.nantian.nbp.saga.SagaManagerCommit;
import com.nantian.nbp.utils.StrUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 事务配置
 *
 * @author JiangTaiSheng
 */
@Configuration
public class SagaTransitionConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SagaTransitionConfig.class);

    @Bean("sagaThreadPoolManager")
    public ThreadPoolManager sagaThreadPoolManager(SqlSessionTemplate sqlSessionTemplate) {
        InitSagaMapper mapper = sqlSessionTemplate.getMapper(InitSagaMapper.class);
        String sagaMgrQueThreadPoolSize = mapper.selectSagaMgrQueThreadPoolSize();
        int size = Integer.parseInt(StrUtils.toStr(sagaMgrQueThreadPoolSize, "6"));
        ThreadPool pool = new ThreadPool();
        pool.setThreadNamePrefix("saga");
        pool.setCorePoolSize(size);
        pool.setMaxPoolSize(size);
        ThreadPoolManager manager = ThreadPoolManager.getInstance();
        manager.buildTaskExecutor("sagaPool", pool);
        SagaManagerCommit sagaManagerCommit;
        for (int i = 0; i < size; i++) {
            sagaManagerCommit = new SagaManagerCommit(mapper, Boolean.TRUE);
            manager.submit("sagaPool", sagaManagerCommit);
        }
        return manager;
    }

    @Bean("sagaTransitionResConfirmation")
    public SagaTransitionResConfirmation sagaTransitionResConfirmation() {
        return new DefSagaTransitionResConfirmationImpl();
    }

}
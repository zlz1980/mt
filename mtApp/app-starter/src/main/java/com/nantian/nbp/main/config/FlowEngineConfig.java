package com.nantian.nbp.main.config;

import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.DefExceptionJsonResImpl;
import com.nantian.nbp.flow.engine.service.DefPbLogHandler;
import com.nantian.nbp.flow.engine.service.FlowEngineV2ServiceImpl;
import com.nantian.nbp.flow.engine.service.api.ExceptionResHandler;
import com.nantian.nbp.flow.engine.service.api.FlowEngineService;
import com.nantian.nbp.flow.engine.service.api.PbLogHandler;
import com.nantian.nbp.flow.engine.service.api.PbSequenceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * 事务配置
 *
 * @author JiangTaiSheng
 */
@Configuration
public class FlowEngineConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowEngineConfig.class);

    @Bean("exceptionResHandler")
    public ExceptionResHandler exceptionResHandler() {
        return new DefExceptionJsonResImpl();
    }

    @Bean
    public PbLogHandler pbLogHandler(){
        return new DefPbLogHandler();
    }

    @Bean("pbSequenceHandler")
    public PbSequenceHandler pbSequenceHandler() {
        return new PbSequenceHandler() {
            @Override
            public String generate() {
                return UUID.randomUUID().toString().replaceAll("-", "");
            }
        };
    }

    @Bean(name = "flowEngineService")
    public FlowEngineService<HttpServletResponse> getFlowEngineService(CacheClientApi cacheClientApi,
                                                                       PbLogHandler pbLogHandler,
                                                                       ExceptionResHandler exceptionResHandler) {
        return new FlowEngineV2ServiceImpl(cacheClientApi,pbLogHandler,exceptionResHandler);
    }

}
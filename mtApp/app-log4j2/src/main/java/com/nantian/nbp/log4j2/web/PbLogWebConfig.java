package com.nantian.nbp.log4j2.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* 仅支持POIN依赖的ResteasyContext */
@Configuration
public class PbLogWebConfig implements WebMvcConfigurer {

    private final PbLogHandlerInterceptor pbLogHandlerInterceptor;

    public PbLogWebConfig(PbLogHandlerInterceptor pbLogHandlerInterceptor) {
        this.pbLogHandlerInterceptor = pbLogHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pbLogHandlerInterceptor)
                .addPathPatterns("/tran/**") // 指定拦截的路径
                .excludePathPatterns("/api/login"); // 排除不需要拦截的路径
    }
}

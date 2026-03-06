package com.nantian.nbp.main.config;

import com.nantian.nbp.main.filter.ReqCountInterceptor;
import com.nantian.nbp.main.filter.AppInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 移除@Resource注解
@Configuration
public class WebAdminConfig implements WebMvcConfigurer {

    @Bean
    public AppInfo appInfo(){
        return new AppInfo();
    }

    @Bean
    public ReqCountInterceptor reqCountInterceptor(AppInfo appInfo){
        return new ReqCountInterceptor(appInfo);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 直接调用@Bean方法（Spring会自动关联容器中的Bean，而非新建）
        registry.addInterceptor(reqCountInterceptor(appInfo()))
                .addPathPatterns("/tran/**");
    }
}
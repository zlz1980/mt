/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.main.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 初始化环境变量
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            System.out.println("初始化变量");
        // 初始化完成
        } else if (event instanceof ApplicationPreparedEvent) {
            System.out.println("初始化完成");
        // 应用刷新
        } else if (event instanceof ContextRefreshedEvent) {
        // 应用已启动完成
        } else if (event instanceof ApplicationReadyEvent) {
            System.out.println("应用已经启动");
        // 应用启动，需要在代码动态添加监听器才可捕获
        } else if (event instanceof ContextStartedEvent) {
        // 应用停止
        } else if (event instanceof ContextStoppedEvent) {
            System.out.println("应用已经停止");
        // 应用关闭
        } else if (event instanceof ContextClosedEvent) {
            System.out.println("应用已经关闭");
        }
    }


}

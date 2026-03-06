package com.nantian.nbp.log4j2.web;

import com.nantian.nbp.log4j2.ThreadContextRouting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PbLogHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbLogHandlerInterceptor.class);

    private final ThreadContextRouting threadContextRouting;

    public PbLogHandlerInterceptor(ThreadContextRouting threadContextRouting) {
        this.threadContextRouting = threadContextRouting;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 在请求处理之前执行
        try {
            threadContextRouting.tranLogRoute(request);
        }catch (Exception e){
            LOGGER.error("PbLogFilter.Tran log failed",e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 在视图渲染之后执行的逻辑
        threadContextRouting.afterClear();
    }
}

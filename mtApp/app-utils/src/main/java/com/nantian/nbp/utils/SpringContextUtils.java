/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
/**
 * @author nantian
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	private static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param name bean名称
	 * @return bean对象
     */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name,Class<T> requiredType){
		return applicationContext.getBean(name,requiredType);
	}

	/**
	 * @param type bean类型
	 * @return bean对象
	 */
	public static <T> T getBean(Class<T> type) throws BeansException{
		return applicationContext.getBean(type);
	}
	
	/**
	 * @param type bean类型
	 * @return bean对象Map集合
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> type){
		return applicationContext.getBeansOfType(type);
	}
	
}

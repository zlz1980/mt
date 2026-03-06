/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarUtil {
	private static final DateTimeFormatter HH_MM_SS_FORMAT = DateTimeFormatter.ofPattern("HHmmss");
	private static final DateTimeFormatter YYYY_MM_DD_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

	/**
	 * 获取当前日期，如20151020
	 * @return String
	 */
	public static String getDate(){
		LocalDateTime localDateTime = getNow();
		return YYYY_MM_DD_FORMAT.format(localDateTime);
	}

	public static String getDate(String formatStr){
		LocalDateTime localDateTime = getNow();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
		return formatter.format(localDateTime);
	}

	public static String getTime(){
		LocalDateTime localDateTime = getNow();
		return HH_MM_SS_FORMAT.format(localDateTime);
	}

	private static LocalDateTime getNow(){
		return LocalDateTime.now();
	}
}


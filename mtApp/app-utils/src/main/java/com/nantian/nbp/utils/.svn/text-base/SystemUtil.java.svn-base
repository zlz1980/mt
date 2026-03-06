/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.utils;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;

/**
 * 获取系统环境变量
 * @author Administrator
 */
public class SystemUtil {

	private SystemUtil() {
	}
	
	public static String jvmEncoding(){
		return Charset.defaultCharset().displayName();
	}
	
	public static String javaVersion(){
		return System.getProperty("java.version");
	}
	
	public static String javaHome(){
		return System.getProperty("java.home");
	}
	
	public static String javaClassPath(){
		return System.getProperty("java.class.path");
	}
	
	public static String javaLibPath(){
		return System.getProperty("java.library.path");
	}
	
	public static String javaIoTmpdir(){
		return System.getProperty("java.io.tmpdir");
	}
	
	public static String osName(){
		return System.getProperty("os.name");
	}
	
	public static String userName(){
		return System.getProperty("user.name");
	}
	
	public static String userHome(){
		return System.getProperty("user.home");
	}
	
	public static String userDir(){
		return System.getProperty("user.dir");
	}
	
	public static String fileSeparator(){
		return FileSystems.getDefault().getSeparator();
	}
	
	public static String pathSeparator(){
		return File.pathSeparator;
	}
	
	public static String lineSeparator(){
		return System.lineSeparator();
	}
	
	public static String hostAddress(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "UnknownHost";
		}
	}
}

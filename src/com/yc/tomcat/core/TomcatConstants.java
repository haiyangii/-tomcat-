package com.yc.tomcat.core;

/**
 * 常量定义
 * @author 易海洋
 * @date   2020年8月22日
 */
public class TomcatConstants {
	public static final String REQUEST_METHOD_GET ="GET";//get请求
	
	public static final String REQUEST_METHOD_POST="POST";//post请求
	
	public static final long SESSION_TIMEOUT = 10 *1000;//session的过期时间，这里为10秒
	
	public static final String REQUEST_JSESSION ="SessionID";//Cookie 的JSESSION的名称
	
	public static final String BASE_PATH = ReadConfig.getInstance().getProperty("path");//基址路径
	
	public static final String DEFAULT_RESOURCE= ReadConfig.getInstance().getProperty("default");//默认变量
}

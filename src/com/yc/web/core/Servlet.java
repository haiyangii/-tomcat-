package com.yc.web.core;

/**
 * servlet接口
 * @author 易海洋
 * @date   2020年8月22日
 */
public interface Servlet {
	
	public void init();

	public void service(ServletRequest request, ServletResponse response);
	
	public void doGet(ServletRequest request, ServletResponse response);
	public void doPost(ServletRequest request, ServletResponse response);
}

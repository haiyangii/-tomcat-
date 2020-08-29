package com.yc.web.core;

import com.yc.tomcat.core.TomcatConstants;

/**
 * 基于http协议的Servlet
 * @author 易海洋
 * @date   2020年8月22日
 */
public class HttpServlet implements Servlet{

	@Override
	public void init() {
		
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) {
		switch(request.getMethod()) {
		case TomcatConstants.REQUEST_METHOD_GET:doGet(request,response);break;
		case TomcatConstants.REQUEST_METHOD_POST:doGet(request,response);break;
		}
	}

	@Override
	public void doGet(ServletRequest request, ServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPost(ServletRequest request, ServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	

}

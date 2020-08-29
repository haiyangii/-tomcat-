package com.yc.web.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yc.tomcat.core.TomcatConstants;

/**
 * 基于http协议的请求处理类
 * @author 易海洋
 * @date   2020年8月22日
 */
public class HttpServletRequest implements ServletRequest{
    private String method;//请求的方式
    private Map<String,String> parameter = new HashMap<String,String>();
    private String url;//请求的资源地址
    private String protoalVersion;
    private BufferedReader read;
    private InputStream is;//请求流
    
    private volatile HttpSession session = new HttpSession();
    private boolean checkJSessionId = false;
    private String jsessionid;
    private Cookie[] cookies;
	
    public String getProtoalVersion() {
		return protoalVersion;
	}
	public HttpServletRequest(InputStream is) {
    	this.is=is;
    }
	@Override
	public String getParameter(String key) {
		return this.parameter.getOrDefault(key, null);
	}

	/**
	 * 解析请求头
	 */
	@Override
	public void parse() {
		try {
		    read = new BufferedReader(new InputStreamReader(is));
			String line = null;
			List<String> headStrs = new ArrayList<String>();
			while((line = read.readLine())!= null && !"".equals(line)) {//读到空行说明请求头结束了
				headStrs.add(line);
			}
			
			parseFristLine(headStrs.get(0));
			parseParameter(headStrs);//解析获取参数
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/**
	 * 解析起始行的
	 * @param string
	 */
	private void parseFristLine(String str) {
		String[] strs = str.split(" ");
		this.method = strs[0];
		if(strs[1].contains("?")) {
			this.url = strs[1].substring(0,strs[1].indexOf("?"));
		}else {
			this.url = strs[1];
		}
		
		this.protoalVersion = strs[2];
		
	}
	
	/**
	 * 解析获取参数
	 * @param headStrs
	 */
	private void parseParameter(List<String> headStrs) {
		//处理请求地址中的参数
		String str = headStrs.get(0).split(" ")[1];//获取请求地址
		if(str.contains("?")) {
			str = str.substring(str.indexOf("?") + 1);//获取请求参数
			String[] params = str.split("&");
			String[] temp;
			for(String param : params) {
				temp = param.split("=");
				this.parameter.put(temp[0],temp[1]);
			}
		}
		if(TomcatConstants.REQUEST_METHOD_POST.equals(this.method)){//说明市post请求
			
			int len=0;
			for(String head : headStrs) {//获取头部协议Content-Lenth
				if(head.contains("Content-Length")) {
					len = Integer.parseInt(head.substring(head.indexOf(":")+1).trim());
					
					break;
				}
			}
			
			if(len <=0 ) {
				return;
			}
			
			//如果要处理文件上传，则还需要获取Content-Type头部掌控
			
			try {
				char[] ch = new char[1024 * 10];
				int count = 0, total =0;//count是每一次读到的大小，total是读到的总大小
				
				StringBuffer sbf = new StringBuffer(1024 * 10);
				while((count = read.read(ch)) > 0) {
					sbf.append(ch,0,count);
					total +=count;
					if(total >=len) {//如果读到的总大小已经大于或等于需要读到的大小了，则跳出循环
						break;
					}
				}
				str = URLDecoder.decode(sbf.toString(),"utf-8");
				str = str.substring(str.indexOf("?") + 1);//获取参数
				String[] params =str.split("&");
				String[] temp;
				for (String param:params) {
					temp = param.split("=");
					this.parameter.put(temp[0],temp[1]);
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	@Override
	public String getUrl() {
		return this.url;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public HttpSession getSession() {
		return this.session;
	}

	@Override
	public Cookie[] getCookies() {
		return this.cookies;
	}

	@Override
	public boolean checkJSessionId() {
	   return this.checkJSessionId;
	}

	@Override
	public String getJSessionId() {
		return this.jsessionid;
	}
	
	

}

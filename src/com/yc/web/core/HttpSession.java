package com.yc.web.core;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * HttpSession
 * @author 易海洋
 * @date   2020年8月22日
 */
public class HttpSession {
    public Map<String,Object> session = new Hashtable<String,Object>();
    private String jsessionid = null;
    
    /**
     * 往session中存值
     * @param key
     * @param value
     */
    public void setAttribute(String key,Object value) {
    	this.session.put(key,value);
    	
    	//刷新服务器里的总session -> 一个jsession对应一个session对象
    	
    	//更新最后一次访问时间
    }
    
    /**
     * 从session中根据键获取对应的值
     * @param key
     * @return
     */
    public Object getAttribute(String key) {
    	if(!(session.containsKey(key))) {
    		return null;
    	}
		return session.get(key);
    }
    
    public void setJSessionId(String jsession) {
    	this.jsessionid = jsessionid;
    }
}

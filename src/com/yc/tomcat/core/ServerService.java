package com.yc.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import com.yc.web.core.HttpServletRequest;
import com.yc.web.core.HttpServletResponse;
import com.yc.web.core.Servlet;
import com.yc.web.core.ServletRequest;
import com.yc.web.core.ServletResponse;

/**
 * 提供服务的方法
 * @author 易海洋
 * @date   2020年8月23日
 */
public class ServerService implements Runnable {
    private Socket sk;
    private InputStream is;
    private OutputStream os;
	
    public ServerService(Socket sk) {
    	this.sk = sk;
    }
    
	@Override
	public void run() {
		try {
			this.is = sk.getInputStream();
			this.os = sk.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;//如果出错则终止运行
		}
		
		//处理请求
		ServletRequest request = new HttpServletRequest(this.is);
		
		//解析请求
		request.parse();
		
		//处理请求
		//请求的servlet还是静态资源，那如何判断市动态资源呢？如果市要映射到动态资源，则肯定会配置到对应项目的web.xml文件中
		//所以，我们必须在服务器启动的时候就自动扫描每个项目下的web.xml文件，解析其中的映射配置
		String url = request.getUrl();
		
		String urlStr = url.substring(1);//去掉前面的/
		String projectName = url.substring(0,url.indexOf("/"));
		
		ServletResponse response = new HttpServletResponse("/"+projectName,os);
		//是不是动态地址
		String clazz = ParseUrlPattern.getClass(url);//如果能取到处理类，则说明是动态资源
		
		if(clazz == null || "".equals(clazz)) {//当成静态资源
			response.sendRedirect(url);
			return;
		}
		
		/**
		 * 处理动态资源的规则
		 * 自己的规则：所欲的动态处理代码->servlet代码必须放到当前项目的bin目录中
		 */
		//要动态加载当前被访问的bin目录中的类
		URLClassLoader loader = null;//类加载器
		URL classPath = null;//要加载这个类的地址
		//url = /DayFresh/
		
		
		try {
			classPath = new URL("file",null,TomcatConstants.BASE_PATH + "\\" +projectName+ "\\ bin");
			
			//创建一个类加载器，告诉他到这个类下加载类
			loader = new URLClassLoader(new URL[] {classPath});
			
			//通过类加载器，加载我们需要的这个类
			Class<?> cls = loader.loadClass(clazz);
			Servlet servlet = (Servlet) cls.newInstance();
			//将这个请求交给Servlet的service()方法处理
			servlet.service(request,response);
		} catch (Exception e) {
			send500(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 500错误
	 * @param e
	 */
	private void send500(Exception e) {
		try {
			String msg = "HTTP/1.1 500 Error\r\n\r\n" + e.getMessage();
			os.write(msg.getBytes());
			os.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			if(os !=null) {
				try {
					os.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			if(sk !=null) {
				try {
					sk.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

}

package com.hooro.ri.config.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class InterceptorConfig implements HandlerInterceptor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override  
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {  

	}  

	@Override  
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {  
	}   

	@SuppressWarnings("rawtypes")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		
		log.error("接口名称："+request.getRequestURI());
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements())
		{  
		String paraName=(String)enu.nextElement();  
		if(paraName.indexOf("[")==-1)
			log.info("请求体key="+paraName+"  value=:"+request.getParameter(paraName));
		}
		
		Enumeration enu1=request.getHeaderNames();  
		while(enu.hasMoreElements())
		{  
		String paraName=(String)enu1.nextElement();  
		if(paraName.indexOf("[")==-1)
			log.info("请求头key="+paraName+"  value=:"+request.getHeader(paraName));
		}
		
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		String requestURI = request.getRequestURI();
		if (requestURI.indexOf("login") != -1||requestURI.indexOf("Interface")!=-1||requestURI.indexOf("upload")!=-1) 
			return true;
		else if(userId!=null)
			    return true;
		request.getRequestDispatcher("/login.jsp").forward(request, response);
		return false;
	}


}

package com.hooro.ri.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** 
* @Description: TODO(难受啊、马飞飞) 
* @author ty 
* @date 2018-4-26 上午11:07:39 
*  
*/
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {    
    
	
	//注册自定义拦截器，添加拦截路径和排除拦截路径    
    @Override    
    public void addInterceptors(InterceptorRegistry registry) {    
       registry.addInterceptor(new InterceptorConfig()).addPathPatterns("/**").excludePathPatterns("/login");    
       super.addInterceptors(registry); 
    }    
    
    //静态资源路径、此处才可作用到外部war发布
    @Override 
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
    	registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
 
}   
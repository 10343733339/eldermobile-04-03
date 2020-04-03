package com.hooro.ri.config;
import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
/** 
* @Description: TODO(上传配置) 
* @author ty 
* @date 2018-3-21 上午9:36:53 
*  
*/
@Configuration
public class MultipartConfig {
 
  /**
 * springboot里默认使用tomcat的上传文件大小限制，即1MB， 修改用下面的配置类：
 */
@Bean
  public MultipartConfigElement multipartConfigElement(){
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize("10MB");
    factory.setMaxRequestSize("10MB");
    return factory.createMultipartConfig();
  }
 
}
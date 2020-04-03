package com.hooro.ri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration //逐层的往下搜索各个加注解的类
@ComponentScan           //扫描Controller 不写路径默认扫描当前目录及下级目录
@ServletComponentScan    // 扫描使用注解方式的servlet
@EnableCaching
//@EnableTransactionManagement  //开启注解事物
@SpringBootApplication
@Configuration
//@ImportResource(locations={"classpath:transactional.xml"})//加载事物配置

public class SampleController extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SampleController.class);

	}
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleController.class, args);

	}


}
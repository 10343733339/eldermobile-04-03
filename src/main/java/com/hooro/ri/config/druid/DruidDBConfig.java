package com.hooro.ri.config.druid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;


/**
 * DruidDBConfig类被@Configuration标注，用作配置信息； 
 * DataSource对象被@Bean声明，为Spring容器所管理， 
 * @Primary表示这里定义的DataSource将覆盖其他来源的DataSource。
 * @author ty
 *jdbc.url=${jdbc.url} 
 *最新的支持方式如下: 
 *jdbc.url=@jdbc.url@ 
 */
@Configuration
public class DruidDBConfig {
	// private Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;

	@Value("${spring.datasource.initialSize}")
	private int initialSize;

	@Value("${spring.datasource.minIdle}")
	private int minIdle;

	@Value("${spring.datasource.maxActive}")
	private int maxActive;

	@Value("${spring.datasource.maxWait}")
	private int maxWait;

	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;

	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;

	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;

	@Value("${spring.datasource.filters}")
	private String filters;

	@Value("{spring.datasource.connectionProperties}")
	private String connectionProperties;
	@Value("${spring.jpa.hibernate.show_sql}")
	private String showSql;

	@Value("${spring.jpa.hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto;

	@Value("${spring.jpa.hibernate.autoReconnect}")
	private String autoReconnect;

	@Value("${spring.jpa.hiberate.dialect}")
	private String dialect;

	@Value("${spring.jpa.hibernate.id.new_generator_mappings}")
	private String generatorMappings;

	/*
	@Value("${spring.jpa.hibernate.id.new_generator_mappings}")
	private String generatorMappings;*/

	/**
	 *    bean    配置之后使用注解注入 SessionFactory
	 */
	/*@Bean
  public HibernateJpaSessionFactoryBean SessionFactory() {
      return new HibernateJpaSessionFactoryBean();
  }*/

	@Bean // 声明其为Bean实例
	@Primary // 在同样的DataSource中，首先使用被标注的DataSource
	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();

		datasource.setUrl(this.dbUrl);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClassName);
		// configuration
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
		}
		datasource.setConnectionProperties(connectionProperties);
		return datasource;
	}
	/**
	 * 配置sessionFactory
	 */
	@Bean("sessionFactory")
	public SessionFactory sessionFactory(DataSource dataSource)
	{
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setPackagesToScan("com.hooro.ri.model");
		sessionFactoryBean.setHibernateProperties(hibProperties());
		//sessionFactoryBean.setHibernateProperties(hibProperties());
		try {
			// 设置完属性之后需要调用 afterPropertiesSet方法使配置生效
			sessionFactoryBean.afterPropertiesSet();
		} catch (IOException e) {
		}
		return sessionFactoryBean.getObject();
	}

	/**
	 * 声明事物
	 */
	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
	{
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}

	/**
	 * 配置aop
	 */
	@Bean
	public BeanNameAutoProxyCreator txProxy() 
	{
		BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
		creator.setInterceptorNames("txAdvice");
		creator.setBeanNames("*Service", "*ServiceImpl");
		creator.setProxyTargetClass(true);
		return creator;
	}

	/**
	 * 配置事务方法
	 */
	
	@Bean(name = "txAdvice")
	public TransactionInterceptor getAdvisor(HibernateTransactionManager transactionManager)
	{

		Properties properties = new Properties();
		//properties.setProperty("*", "PROPAGATION_REQUIRED,-Exception,readOnly");
		properties.setProperty("get*", "PROPAGATION_REQUIRED,-Exception,readOnly");
		properties.setProperty("find*", "PROPAGATION_REQUIRED,-Exception,readOnly");
		properties.setProperty("check*", "PROPAGATION_REQUIRED,-Exception,readOnly");
		properties.setProperty("show*", "PROPAGATION_REQUIRED,-Exception,readOnly");

		properties.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
		properties.setProperty("_save*", "PROPAGATION_REQUIRED,-Exception");
		properties.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
		properties.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
		properties.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
		TransactionInterceptor tsi = new TransactionInterceptor(transactionManager,properties);
		return tsi;
	}

	/** 
	 * 注册一个StatViewServlet 
	 * @return 
	 */  
	@Bean  
	public ServletRegistrationBean DruidStatViewServle2(){  
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");  

		//添加初始化参数：initParams  
		/** 白名单，如果不配置或value为空，则允许所有 */  
		//servletRegistrationBean.addInitParameter("allow","127.0.0.1,192.0.0.1");  
		/** 黑名单，与白名单存在相同IP时，优先于白名单 */  
		//servletRegistrationBean.addInitParameter("deny","192.0.0.1");  
		/** 用户名 */  
		servletRegistrationBean.addInitParameter("loginUsername","admin");  
		/** 密码 */  
		servletRegistrationBean.addInitParameter("loginPassword","admin");  
		/** 禁用页面上的“Reset All”功能 */  
		servletRegistrationBean.addInitParameter("resetEnable","false");  
		return servletRegistrationBean;  
	}  

	/** 
	 * 注册一个：WebStatFilter 
	 * @return 
	 */  
	@Bean  
	public FilterRegistrationBean druidStatFilter2(){  
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());  

		/** 过滤规则 */  
		filterRegistrationBean.addUrlPatterns("/*");  
		/** 忽略资源 */  
		filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");  
		return filterRegistrationBean;  
	}  

	/**
	 * 生成hibernate properties
	 */

	private Properties hibProperties() 
	{
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", dialect);
		properties.setProperty("hibernate.show_sql", showSql);
		properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
		properties.setProperty("hibernate.autoReconnect", autoReconnect);
		properties.setProperty("hibernate.id.new_generator_mappings", generatorMappings);
		return properties;
	}


}
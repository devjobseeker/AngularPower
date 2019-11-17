package org.asu.chilll.power.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	
	@Bean(name = "localDS")
	@ConfigurationProperties("local.datasource")
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "centerDS")
	@ConfigurationProperties("center.datasource")
	public DataSource dataSource2() {
		return DataSourceBuilder.create().build(); 
	}
	
//	@Bean(name="entityManagerFactory")
//	@Primary
//	public LocalContainerEntityManagerFactoryBean entityManagerFactory1(EntityManagerFactoryBuilder builder) {
//		//Properties hibernateProperties = new Properties();
//		//hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
//		//hibernate.hbm2ddl.auto
//		System.out.println("=============== create entity mamager factory 1 =============");
//		Map<String, String> propMap = new HashMap<String, String>();
//		propMap.put("hibernate.hbm2ddl.auto", "update");
//		propMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
//		return builder.dataSource(dataSource())
//				.persistenceUnit("entityManagerFactory")
//				.packages("org.asu.chilll.power.entity")
//				.properties(propMap)
//				.build();
//	}
	
	@Bean(name="entityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean localEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "org.asu.chilll.power.entity" });
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(adapter);
		
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("hibernate.hbm2ddl.auto", "update");
		propMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		em.setJpaPropertyMap(propMap);
		return em;
	}
	
	@Bean(name = "transactionManager")
	@Primary
	public PlatformTransactionManager localTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(localEntityManagerFactory().getObject());
		return transactionManager;
	}
	
	//disable for play game version
	@Bean(name="centerEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean centerEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource2());
		em.setPackagesToScan(new String[] { "org.asu.chilll.power.entity" });
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(adapter);
		
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("hibernate.hbm2ddl.auto", "update");
		propMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		em.setJpaPropertyMap(propMap);
		return em;
	}
	
	@Bean(name = "centerTransactionManager")
	public PlatformTransactionManager centerTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(centerEntityManagerFactory().getObject());
		return transactionManager;
	}
}

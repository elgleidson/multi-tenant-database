package com.github.elgleidson.multi.tenant.database.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.elgleidson.multi.tenant.database.domain.core.Tenant;
import com.github.elgleidson.multi.tenant.database.repository.core.TenantRepository;
import com.github.elgleidson.multi.tenant.database.util.DataSourceUtil;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages= {
		"com.github.elgleidson.multi.tenant.database.domain.core",
		"com.github.elgleidson.multi.tenant.database.repository.core"
	},
	entityManagerFactoryRef="coreEntityManagerFactory",
	transactionManagerRef="coreTransactionManager"
)
public class CoreDatabaseConfig {
	
	public static final String DATA_SOURCE_NAME = "coreDataSource";
	
	@Autowired
	private DataSourceProperties dataSourceProperties;
	
	@Autowired
    private JpaProperties jpaProperties;
	
	@Primary
	@Bean("coreDataSource")
	public DataSource coreDataSource() {
		return DataSourceUtil.createDataSource(
			dataSourceProperties.getUrl(), 
			dataSourceProperties.getUsername(), 
			dataSourceProperties.getPassword()
		);
	}
	
	@Primary
	@Bean("coreEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean coreEntityManagerFactory() {
		Map<String, Object> jpaPropertiesMap = new HashMap<>();
		jpaPropertiesMap.putAll(jpaProperties.getProperties());
		
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(coreDataSource());
		em.setPackagesToScan(Tenant.class.getPackage().getName(), TenantRepository.class.getPackage().getName());
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaPropertyMap(jpaPropertiesMap);
		em.setPersistenceUnitName("corePersistenceUnit");
		
		return em;
	}
	
	@Primary
	@Bean("coreTransactionManager")
	public JpaTransactionManager coreTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(coreEntityManagerFactory().getObject());
		return jpaTransactionManager;
	}
	
}

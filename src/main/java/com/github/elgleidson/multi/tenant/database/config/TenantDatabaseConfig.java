package com.github.elgleidson.multi.tenant.database.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.elgleidson.multi.tenant.database.domain.tenant.Demo;
import com.github.elgleidson.multi.tenant.database.multitenant.MultiTenantDatabaseProvider;
import com.github.elgleidson.multi.tenant.database.multitenant.MultiTenantDatabaseResolver;
import com.github.elgleidson.multi.tenant.database.repository.tenant.DemoRepository;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackageClasses = { Demo.class, DemoRepository.class },
	entityManagerFactoryRef="tenantEntityManagerFactory",
	transactionManagerRef="tenantTransactionManager"
)
public class TenantDatabaseConfig {
	
	@Autowired
    private JpaProperties jpaProperties;
	
	@Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
	
	@Bean("tenantEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
			MultiTenantDatabaseProvider multiTenantDatabaseProvider,
			MultiTenantDatabaseResolver multiTenantDatabaseResolver) {
		Map<String, Object> jpaPropertiesMap = new HashMap<>();
		jpaPropertiesMap.putAll(jpaProperties.getProperties());
		jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantDatabaseProvider);
		jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, multiTenantDatabaseResolver);

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setPackagesToScan(Demo.class.getPackage().getName(), DemoRepository.class.getPackage().getName());
		em.setJpaVendorAdapter(jpaVendorAdapter());
		em.setJpaPropertyMap(jpaPropertiesMap);
		em.setPersistenceUnitName("tenantPersistenceUnit");
		
		return em;
	}
	
	@Bean("tenantTransactionManager")
	public JpaTransactionManager coreTransactionManager(
			@Qualifier("tenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
}

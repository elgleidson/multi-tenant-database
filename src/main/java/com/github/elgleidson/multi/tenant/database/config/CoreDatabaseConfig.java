package com.github.elgleidson.multi.tenant.database.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.elgleidson.multi.tenant.database.domain.core.User;
import com.github.elgleidson.multi.tenant.database.repository.core.UserRepository;
import com.github.elgleidson.multi.tenant.database.util.DataSourceUtil;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackageClasses = { User.class, UserRepository.class },
	entityManagerFactoryRef="coreEntityManagerFactory",
	transactionManagerRef="coreTransactionManager"
)
public class CoreDatabaseConfig {
	
	@Autowired
	private DataSourceProperties dataSourceProperties;
	
	@Primary
	@Bean("coreDataSource")
	public DataSource dataSource() {
		return DataSourceUtil.createDataSource(
				dataSourceProperties.getUrl(), 
				dataSourceProperties.getUsername(), 
				dataSourceProperties.getPassword()
		);
	}

	
	@Primary
	@Bean("coreEntityManagerFactory")
//	@ConditionalOnBean(name="coreDataSource")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder builder, 
			@Qualifier("coreDataSource") DataSource dataSource) {
		return builder
				.dataSource(dataSource)
				.packages(User.class, UserRepository.class)
				.persistenceUnit("corePersistenceUnit")
				.build();
	}
	
	@Primary
	@Bean("coreTransactionManager")
	public JpaTransactionManager coreTransactionManager(
			@Qualifier("coreEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
}

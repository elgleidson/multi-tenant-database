package com.github.elgleidson.multi.tenant.database.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.elgleidson.multi.tenant.database.util.MultiTenantDatabaseLiquibase;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseConfig {
	
	@Primary
	@Bean("liquibase")
	public SpringLiquibase coreLiquibase(@Qualifier("coreDataSource") DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:/db/changelog/db.changelog-core.xml");
		liquibase.setShouldRun(true);
		
		return liquibase;
	}
	
	@Bean("tenantLiquibase")
	public MultiTenantDatabaseLiquibase liquibaseMultiTenant() {
		MultiTenantDatabaseLiquibase liquibase = new MultiTenantDatabaseLiquibase();
		liquibase.setChangeLog("classpath:/db/changelog/db.changelog-tenants.xml");
		liquibase.setShouldRun(true);

		return liquibase;
	}
}

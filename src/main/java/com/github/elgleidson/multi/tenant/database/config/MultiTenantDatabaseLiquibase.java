package com.github.elgleidson.multi.tenant.database.config;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.github.elgleidson.multi.tenant.database.multitenant.MultiTenantDatabaseProvider;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;

public class MultiTenantDatabaseLiquibase extends MultiTenantSpringLiquibase {
	
	private static final Logger log = LoggerFactory.getLogger(MultiTenantDatabaseLiquibase.class);
	
	@Autowired
	private MultiTenantDatabaseProvider multiTenantDatabaseProvider;
	
	private ResourceLoader resourceLoader;
	
	public MultiTenantDatabaseLiquibase() {
		super();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.runOnAllDataSources();
	}
	
	private void runOnAllDataSources() throws LiquibaseException {
		Map<String, DataSource> dataSources = multiTenantDatabaseProvider.getDataSources();
		for (String tenant: dataSources.keySet()) {
			log.info("Initializing Liquibase for data source {}", tenant);
			SpringLiquibase liquibase = getSpringLiquibase(dataSources.get(tenant));
			liquibase.afterPropertiesSet();
			log.info("Liquibase ran for data source {}", tenant);
		}
	}
	
	private SpringLiquibase getSpringLiquibase(DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(getChangeLog());
		liquibase.setChangeLogParameters(getParameters());
		liquibase.setContexts(getContexts());
		liquibase.setLabels(getLabels());
		liquibase.setDropFirst(isDropFirst());
		liquibase.setShouldRun(isShouldRun());
		liquibase.setRollbackFile(getRollbackFile());
		liquibase.setDataSource(dataSource);
		liquibase.setDefaultSchema(getDefaultSchema());
		liquibase.setResourceLoader(getResourceLoader());
		return liquibase;
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

}

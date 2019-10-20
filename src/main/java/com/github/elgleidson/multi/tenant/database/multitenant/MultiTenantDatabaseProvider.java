package com.github.elgleidson.multi.tenant.database.multitenant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.elgleidson.multi.tenant.database.domain.core.Tenant;
import com.github.elgleidson.multi.tenant.database.exception.TenantNotExistsException;
import com.github.elgleidson.multi.tenant.database.repository.core.TenantRepository;
import com.github.elgleidson.multi.tenant.database.util.DataSourceUtil;

@Component
public class MultiTenantDatabaseProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(MultiTenantDatabaseProvider.class);
	
	private final Map<String, DataSource> tenantDataSources = new HashMap<>();
	
	@Autowired
	private TenantRepository tenantRepository;
	
	public MultiTenantDatabaseProvider() {
		super();
	}
	
	public Map<String, DataSource> getDataSources() {
		if (tenantDataSources.isEmpty()) {
			initTenantDataSourcesMap();
		}
		return tenantDataSources;
	}
	
	@Override
	protected DataSource selectAnyDataSource() {
		if (tenantDataSources.isEmpty()) {
			initTenantDataSourcesMap();
		}
		return tenantDataSources.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		if (tenantDataSources.isEmpty()) {
			initTenantDataSourcesMap();
		}
		
		if (!tenantDataSources.containsKey(tenantIdentifier)) {
			log.info("Tenant not found! Retrieving from database...");
			initTenant(tenantIdentifier);
			log.info("Tenant datasource mapped!");
		}
		
		return tenantDataSources.get(tenantIdentifier);
	}
	
	private void initTenantDataSourcesMap() {
		log.info("Initializing tenant datasources map...");
		tenantRepository.findAll().forEach(tenant -> {
			DataSource dataSource = createDataSource(tenant);
			tenantDataSources.put(tenant.getName(), dataSource);
		});
		log.info("Tenant datasources map initialized!");
	}
	
	private void initTenant(String tenantIdentifier) {
		Optional<Tenant> findByName = tenantRepository.findByName(tenantIdentifier);
		if (findByName.isPresent()) {
			Tenant tenant = findByName.get();
			DataSource dataSource = createDataSource(tenant);
			tenantDataSources.put(tenantIdentifier, dataSource);
		} else {
			throw new TenantNotExistsException("Tenant '" + tenantIdentifier + "' not exists! Check the tenant table!");
		}
	}
	
	private DataSource createDataSource(Tenant tenant) {
		return DataSourceUtil.createDataSource(tenant.getUrl(), tenant.getUsername(), tenant.getPassword());
	}
	
}

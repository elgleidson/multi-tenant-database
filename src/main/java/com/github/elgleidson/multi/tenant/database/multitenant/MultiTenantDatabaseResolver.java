package com.github.elgleidson.multi.tenant.database.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class MultiTenantDatabaseResolver implements CurrentTenantIdentifierResolver {
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		return TenantContextHolder.getCurrentTenant();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}

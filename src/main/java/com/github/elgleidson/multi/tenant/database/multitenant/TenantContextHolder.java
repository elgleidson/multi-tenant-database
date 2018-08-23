package com.github.elgleidson.multi.tenant.database.multitenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContextHolder {
	
	public static final String DEFAULT_TENANT = "core";
	private static final Logger log = LoggerFactory.getLogger(TenantContextHolder.class); 
	private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

	public static String getCurrentTenant() {
		String tenant = currentTenant.get();
		if (tenant == null) {
			return DEFAULT_TENANT;
		}
		return tenant;
	}

	public static void setCurrentTenant(String tenant) {
		currentTenant.set(tenant);
		log.debug("Current tenant = {}", tenant);
	}

	public static void clear() {
		currentTenant.set(null);
	}
	
}

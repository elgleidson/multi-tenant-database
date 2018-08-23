package com.github.elgleidson.multi.tenant.database.repository.core;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.elgleidson.multi.tenant.database.domain.core.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

	Optional<Tenant> findByName(String name);
	
}

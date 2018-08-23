package com.github.elgleidson.multi.tenant.database.repository.core;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.elgleidson.multi.tenant.database.domain.core.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String nome);
	
	boolean existsByName(String nome);
	
}

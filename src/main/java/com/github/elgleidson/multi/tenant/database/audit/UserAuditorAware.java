package com.github.elgleidson.multi.tenant.database.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.elgleidson.multi.tenant.database.domain.core.User;

public class UserAuditorAware implements AuditorAware<Long> {
	
	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null
			|| !authentication.isAuthenticated()
			|| authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}
		User user = (User) authentication.getPrincipal();
		return Optional.of(user.getId());
	}

}

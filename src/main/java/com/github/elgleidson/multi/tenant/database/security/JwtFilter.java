package com.github.elgleidson.multi.tenant.database.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.elgleidson.multi.tenant.database.domain.core.User;
import com.github.elgleidson.multi.tenant.database.multitenant.TenantContextHolder;

public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = getJwt(request);
		if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String tenant = TenantContextHolder.DEFAULT_TENANT;
			User user = (User) authentication.getPrincipal();
			if (user.getTenant() != null) {
				tenant = user.getTenant().getName();
			}
			TenantContextHolder.setCurrentTenant(tenant);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String getJwt(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}

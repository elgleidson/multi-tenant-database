package com.github.elgleidson.multi.tenant.database.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.elgleidson.multi.tenant.database.domain.enumeration.RoleName;
import com.github.elgleidson.multi.tenant.database.security.JwtAuthenticationEntryPoint;
import com.github.elgleidson.multi.tenant.database.security.JwtFilter;
import com.github.elgleidson.multi.tenant.database.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService usuarioService; 
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
	
	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter();
	};

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService)
			.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
				.antMatchers("/api/admin/**").hasAuthority(RoleName.ROLE_ADMIN.name())
				.antMatchers("/api/auth/**").permitAll()
				.antMatchers("/v2/api-docs/**").permitAll()
	            .antMatchers("/swagger-ui.html").permitAll()
	            .antMatchers("/management/**").permitAll()
				.antMatchers("/api/**").authenticated();
		
		http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
}

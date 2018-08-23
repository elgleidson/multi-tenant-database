package com.github.elgleidson.multi.tenant.database.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.elgleidson.multi.tenant.database.domain.core.User;
import com.github.elgleidson.multi.tenant.database.security.JwtTokenProvider;
import com.github.elgleidson.multi.tenant.database.service.UserService;
import com.github.elgleidson.multi.tenant.database.web.dto.ApiResponse;
import com.github.elgleidson.multi.tenant.database.web.dto.LoginRequest;
import com.github.elgleidson.multi.tenant.database.web.dto.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);

    @Autowired
    private UserService usuarioService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody RegisterRequest registerRequest) {
    	log.debug("HTTP request to create new user {}", registerRequest);    	
    	try {
    		User usuario = usuarioService.create(registerRequest);
    		return ResponseEntity.ok(new ApiResponse(true, "User '" + usuario.getUsername() + "' created successfully!"));       	
    	} catch (Exception ex) {
    		return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    	}
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    	log.debug("HTTP request to log in {}", loginRequest);
    	Authentication authentication = authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
    	);
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	String jwt = tokenProvider.generateToken(authentication);
    	return ResponseEntity.ok(jwt);
    }

}

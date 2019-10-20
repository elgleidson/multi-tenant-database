package com.github.elgleidson.multi.tenant.database.service;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.elgleidson.multi.tenant.database.domain.core.Role;
import com.github.elgleidson.multi.tenant.database.domain.core.User;
import com.github.elgleidson.multi.tenant.database.domain.enumeration.RoleName;
import com.github.elgleidson.multi.tenant.database.exception.EmailAlreadyExistsException;
import com.github.elgleidson.multi.tenant.database.exception.RoleNotExistsException;
import com.github.elgleidson.multi.tenant.database.exception.UsernameAlreadyExistsException;
import com.github.elgleidson.multi.tenant.database.repository.core.RoleRepository;
import com.github.elgleidson.multi.tenant.database.repository.core.UserRepository;
import com.github.elgleidson.multi.tenant.database.web.dto.RegisterRequest;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Transactional 
    public User create(RegisterRequest registerRequest) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
    	log.debug("Creating new User {}", registerRequest);
    	if (userRepository.existsByUsername(registerRequest.getUsername())) {
    		throw new UsernameAlreadyExistsException("Username '" + registerRequest.getUsername() + "' already exists!");
    	}
    	if (userRepository.existsByEmail(registerRequest.getEmail())) {
    		throw new EmailAlreadyExistsException("Email '" + registerRequest.getEmail() + "' already exists!");
    	}
    	
    	Role perfil = roleRepository.findByName(RoleName.ROLE_USER.name())
    			.orElseThrow(() -> new RoleNotExistsException("Role '" + RoleName.ROLE_USER.name() + "' not exists!"));
    	
    	User user = new User();
    	user.setName(registerRequest.getName());
    	user.setEmail(registerRequest.getEmail());
    	user.setUsername(registerRequest.getUsername());
    	user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    	user.setActive(true);
    	user.setRoles(Collections.singleton(perfil));
    	user.setId(null);
    	user = userRepository.save(user);
    	log.debug("User created {}", user);
    	return user;
    }
    
    public Optional<User> findById(Long id) {
        log.debug("Finding User by id {}...", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
        	log.debug("Found User {}", user);
        } else {
        	log.debug("User not found by id {}!", id);
        }
        return user;
    }
    
    public Optional<User> findByEmail(String email) {
    	log.debug("Finding User by email {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
        	log.debug("Found User {}", user);
        } else {
        	log.debug("User not found by email {}!", email);
        }
        return user;
    }

    public Optional<User> findByUsername(String username) {
        log.debug("Finding User by username {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
        	log.debug("Found User {}", user);
        } else {
        	log.debug("User not found by username {}!", username);
        }
        return user;
    }
    
    public boolean existsByUsername(String username) {
    	log.debug("Checking exists by username {}", username);
    	return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
    	log.debug("Checking exists by email {}", email);
    	return userRepository.existsByUsername(email);
    }
    
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		return user.orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found in database!"));
	}
    
    public Page<User> findAll(Pageable pageable) {
    	log.debug("Find all User by page {}", pageable);
    	Page<User> page = userRepository.findAll(pageable);
    	return page;
    }

}

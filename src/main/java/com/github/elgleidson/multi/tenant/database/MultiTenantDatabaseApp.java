package com.github.elgleidson.multi.tenant.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
public class MultiTenantDatabaseApp {

    private static final Logger log = LoggerFactory.getLogger(MultiTenantDatabaseApp.class);
    
    public static void main(String[] args) {
    	SpringApplication app = new SpringApplication(MultiTenantDatabaseApp.class);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        log.info("\n------------------------------------------\n" +
        		"Application '{}' is running at localhost:{}\n" + 
        		"------------------------------------------", 
        		env.getProperty("spring.application.name"),
        		env.getProperty("server.port")
        		);
    }

}

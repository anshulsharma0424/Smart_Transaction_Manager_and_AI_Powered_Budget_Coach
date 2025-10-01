package com.spenzr.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * This will be the main entry point for the Eureka Discovery Server.
 * The "@EnableEurekaServer" annotation configures this Spring Boot application to act as a service registry for other microservices.
 */
@SpringBootApplication // A standard Spring Boot annotation that enables auto-configuration and component scanning in the entire application.
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}

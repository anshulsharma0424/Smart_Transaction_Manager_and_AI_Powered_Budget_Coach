package com.spenzr.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Main entry point for the Spring Cloud Config Server.
 * The @EnableConfigServer annotation sets up this application to serve
 * configuration properties to other microservices.
 * The @EnableDiscoveryClient allows this server to register itself with Eureka.
 */

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}

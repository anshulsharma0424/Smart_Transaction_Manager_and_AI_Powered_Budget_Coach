package com.spenzr.group_expense_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GroupExpenseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupExpenseServiceApplication.class, args);
	}

}

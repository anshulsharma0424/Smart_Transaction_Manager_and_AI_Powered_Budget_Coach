package com.spenzr.category_service;

import com.spenzr.category_service.entity.Category;
import com.spenzr.category_service.entity.CategoryType;
import com.spenzr.category_service.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class CategoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoryServiceApplication.class, args);
	}

    // This bean will run on startup and pre-populate our database
    @Bean
    CommandLineRunner loadData(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                System.out.println("No categories found, pre-populating database...");
                // System-wide default categories (userId is null)
                Category c1 = Category.builder().name("Food").type(CategoryType.EXPENSE).build();
                Category c2 = Category.builder().name("Transport").type(CategoryType.EXPENSE).build();
                Category c3 = Category.builder().name("Shopping").type(CategoryType.EXPENSE).build();
                Category c4 = Category.builder().name("Utilities").type(CategoryType.EXPENSE).build();
                Category c5 = Category.builder().name("Salary").type(CategoryType.INCOME).build();
                Category c6 = Category.builder().name("Freelance").type(CategoryType.INCOME).build();
                categoryRepository.saveAll(List.of(c1, c2, c3, c4, c5, c6));
                System.out.println("Pre-population complete.");
            }
        };
    }
}

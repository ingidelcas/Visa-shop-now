package com.visa.userService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.visa.userService.controller", "com.visa.userService..services"
		, "com.visa.lib.config, com.visa.lib.exceptions"
})
@EntityScan(basePackages = {"com.visa.lib.entity"})
@EnableJpaRepositories(basePackages = {"com.visa.lib.repository"})
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}

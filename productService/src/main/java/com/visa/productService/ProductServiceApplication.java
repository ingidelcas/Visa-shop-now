package com.visa.productService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.visa.productService.controller", "com.visa.productService.services"
        , "com.visa.lib.config"
})
@EntityScan(basePackages = {"com.visa.lib.entity"})
@EnableJpaRepositories(basePackages = {"com.visa.lib.repository"})
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}

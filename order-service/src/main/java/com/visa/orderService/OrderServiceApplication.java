package com.visa.orderService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan(basePackages = {"com.visa.orderService.controller", "com.visa.orderService.services"
        , "com.visa.lib.config"
})
@EntityScan(basePackages = {"com.visa.lib.entity"})
@EnableJpaRepositories(basePackages = {"com.visa.lib.repository", "com.visa.orderService.repository"})
@EnableFeignClients
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}

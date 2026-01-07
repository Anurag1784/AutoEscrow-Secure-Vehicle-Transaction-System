package com.autoescrow.escrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.autoescrow")
@EnableScheduling
@EnableFeignClients(basePackages = "com.autoescrow")
@EntityScan(basePackages = "com.autoescrow")
@EnableJpaRepositories(basePackages = "com.autoescrow")
public class EscrowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EscrowServiceApplication.class, args);
    }
}

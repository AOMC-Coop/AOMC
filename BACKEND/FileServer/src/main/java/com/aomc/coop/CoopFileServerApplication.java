package com.aomc.coop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.aomc.coop.storage.StorageProperties;
import com.aomc.coop.service.StorageService;

// @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CoopFileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoopFileServerApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
// *** 서버가 종료되어도 파일은 남아 있어야 하므로, deleteAll() 함수는 실행시키지 않는다.
//            storageService.deleteAll();
            storageService.init();
        };
    }
}


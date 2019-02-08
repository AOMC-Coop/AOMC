package com.aomc.coop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Although it is possible to package this service as a traditional WAR file
// for deployment to an external application server, the simpler approach demonstrated below
// creates a standalone application. You package everything in a single, executable JAR file,
// driven by a good old Java main() method. And along the way, you use Spring’s support
// for embedding the Tomcat servlet container as the HTTP runtime, instead of deploying to an external instance.
// You also want a target folder to upload files to,
// so let’s enhance the basic Application class and add a Boot CommandLineRunner which deletes and re-creates that folder at startup:

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.aomc.coop.storage.StorageProperties;
import com.aomc.coop.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CoopFileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoopFileServerApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}

// @SpringBootApplication is a convenience annotation that adds all of the following:
// @Configuration tags the class as a source of bean definitions for the application context.
// @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
// Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath.
// This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
// @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.
// The main() method uses Spring Boot’s SpringApplication.run() method to launch an application.
// Did you notice that there wasn’t a single line of XML? No web.xml file either.
// This web application is 100% pure Java and you didn’t have to deal with configuring any plumbing or infrastructure.
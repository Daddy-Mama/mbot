package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class DemoApplication {
    private static Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DemoApplication.class, args);

        logger.info("=======================APPLICATION STARTED =====================");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

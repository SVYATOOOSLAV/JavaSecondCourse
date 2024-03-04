package edu.java.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperConfig {
    @Bean
    ApplicationConfig.Scheduler scheduler(ApplicationConfig applicationConfig) {
        return applicationConfig.scheduler();
    }
}

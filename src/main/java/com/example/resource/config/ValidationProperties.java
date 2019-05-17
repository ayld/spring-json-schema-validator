package com.example.resource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "validation")
@EnableConfigurationProperties(ValidationProperties.class)
public class ValidationProperties {

    private String schemaPath;
}

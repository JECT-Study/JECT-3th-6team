package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "custom.app")
public class AppProperties {
    private String frontendUrl;
    private List<String> corsAllowedOrigins;
    private String adminPassword;
} 
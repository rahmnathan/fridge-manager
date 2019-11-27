package com.nathanrahm.fridge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "service")
public class ServiceConfig {
    private Map<String, Integer> fridgeItemMaximums = new HashMap<>();
}

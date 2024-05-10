package com.nikhil.redisdemo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private boolean enabled = false;
    private String host = "localhost";
    private Set<String> slaves;
    private Integer database = 3;
    private String mode = "single";
}

package com.todo.app.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.jwt.config")
public record JwtProperties(
        Duration lifetimeToken,
        String secretKey
) {
}
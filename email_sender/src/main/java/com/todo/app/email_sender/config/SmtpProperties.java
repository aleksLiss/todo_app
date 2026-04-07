package com.todo.app.email_sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record SmtpProperties(
        String from,
        String protocol,
        String host,
        int port,
        String username,
        String password
) {
}

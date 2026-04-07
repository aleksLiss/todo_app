package com.todo.app.email_sender.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final SmtpProperties smtpProperties;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpProperties.host());
        mailSender.setPort(smtpProperties.port());
        mailSender.setUsername(smtpProperties.username());
        mailSender.setPassword(smtpProperties.password());
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", smtpProperties.protocol());
        return mailSender;
    }
}

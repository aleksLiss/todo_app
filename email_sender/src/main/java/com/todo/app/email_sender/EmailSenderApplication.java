package com.todo.app.email_sender;

import com.todo.app.email_sender.config.KafkaProperties;
import com.todo.app.email_sender.config.SmtpProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProperties.class, SmtpProperties.class})
public class EmailSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailSenderApplication.class, args);
    }
}

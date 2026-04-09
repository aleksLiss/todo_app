package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.config.SmtpProperties;
import com.todo.app.email_sender.dto.KafkaMessageDto;
import com.todo.app.email_sender.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailCreatorService implements CreatorService {

    private final SmtpProperties smtpProperties;

    @Override
    public SimpleMailMessage create(KafkaMessageDto kafkaMessageDto) {
        String from = smtpProperties.from();
        String to = kafkaMessageDto.email();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setText(kafkaMessageDto.body());
        simpleMailMessage.setSubject(kafkaMessageDto.title());
        simpleMailMessage.setTo(to);
        return simpleMailMessage;
    }
}

package com.todo.app.email_sender.mapper;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.mail.SimpleMailMessage;

@Mapper(componentModel = "spring")
public interface SimpleMailMassageMapper {

    @Mapping(target = "to", source = "email")
    @Mapping(target = "subject", source = "title")
    @Mapping(target = "text", source = "body")
    @Mapping(target = "from", ignore = true)
    SimpleMailMessage toSimpleMailMessage(KafkaMessageDto kafkaMessageDto);
}

package com.todo.app.email_sender.model;

import com.todo.app.email_sender.dto.KafkaMessageDto;

public record MessageReceivedEvent(KafkaMessageDto dto) {
}

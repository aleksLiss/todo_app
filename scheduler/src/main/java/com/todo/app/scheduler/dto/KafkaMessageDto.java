package com.todo.app.scheduler.dto;

public record KafkaMessageDto(
        String email,
        String title,
        String body
) {
}
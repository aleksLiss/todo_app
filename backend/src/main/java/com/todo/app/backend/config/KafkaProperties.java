package com.todo.app.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String bootstrapServers,
        String topicName,
        int partitionsCount,
        int replicationCount
) {
}

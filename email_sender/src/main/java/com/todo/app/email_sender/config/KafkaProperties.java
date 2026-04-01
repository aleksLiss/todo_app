package com.todo.app.email_sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String bootstrapServers,
        Consumer consumer,
        String topicName,
        int partitionsCount,
        int replicationCount
) {
    public record Consumer(
            String groupId,
            String autoOffsetReset,
            String keySerializer,
            String valueSerializer
    ) {
    }
}

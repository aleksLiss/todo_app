package com.todo.app.email_sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String bootstrapServers,
        String topicName,
        int partitionsCount,
        int replicationCount,
        Consumer consumer
) {
    public record Consumer(
            String groupId,
            String autoOffsetReset
    ) {
    }
}

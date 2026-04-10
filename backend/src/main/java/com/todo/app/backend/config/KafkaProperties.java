package com.todo.app.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String bootstrapServers,
        Producer producer,
        String topicName,
        String partitionsCount,
        String replicationCount
) {
    public record Producer(
            String keySerializer,
            String valueSerializer
    ) {
    }
}

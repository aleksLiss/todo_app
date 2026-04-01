package com.todo.app.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String bootstrapServers,
        Producer producer,
        String topicName,
        int partitionsCount,
        int replicationCount
) {
    public record Producer(
            String keySerializer,
            String valueSerializer
    ) {
    }
}

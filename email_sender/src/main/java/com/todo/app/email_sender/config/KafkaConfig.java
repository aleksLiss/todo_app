package com.todo.app.email_sender.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(kafkaProperties.topicName())
                .partitions(kafkaProperties.partitionsCount())
                .replicas(kafkaProperties.replicationCount())
                .build();
    }

    @Bean
    public ConsumerFactory<@NonNull String,@NonNull String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.consumer().groupId());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.consumer().keyDeserializer());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.consumer().valueDeserializer());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.consumer().autoOffsetReset());
        return new DefaultKafkaConsumerFactory<>(config);
    }
}

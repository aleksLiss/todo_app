package com.todo.app.email_sender.config;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    public ConsumerFactory<String, KafkaMessageDto> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.consumer().groupId());

        // Настраиваем десериализатор вручную
        JsonDeserializer<KafkaMessageDto> jsonDeserializer = new JsonDeserializer<>(KafkaMessageDto.class);

        // ИГНОРИРУЕМ заголовки типов (это решит проблему с String)
        jsonDeserializer.setUseTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessageDto> kafkaListenerContainerFactory(
            ConsumerFactory<String, KafkaMessageDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}

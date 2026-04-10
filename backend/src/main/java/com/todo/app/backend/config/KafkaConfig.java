package com.todo.app.backend.config;

import com.todo.app.backend.dto.KafkaMessageDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Bean
    public ProducerFactory<@NonNull String, @NonNull String> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.producer().keySerializer());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.producer().valueSerializer());
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<@NonNull String,@NonNull KafkaMessageDto> kafkaTemplate(ProducerFactory<@NonNull String, @NonNull KafkaMessageDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}

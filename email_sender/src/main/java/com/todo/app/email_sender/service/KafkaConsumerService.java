package com.todo.app.email_sender.service;

import com.todo.app.email_sender.config.KafkaProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final KafkaProperties kafkaProperties;
    private final ConsumerFactory<@NonNull String,@NonNull String> consumerFactory;

    public void receiveMessage(String message){
        try(Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singletonList(kafkaProperties.topicName()));
            while (true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Получено вручную: Key={}, Value={}", record.key(), record.value());
                }
                consumer.commitSync();
            }
        }
    }
}

package com.todo.app.scheduler.service;

import com.google.gson.Gson;
import com.todo.app.scheduler.config.KafkaProperties;
import com.todo.app.scheduler.dto.KafkaMessageDto;
import com.todo.app.scheduler.exception.KafkaSendMessageException;
import com.todo.app.scheduler.mapper.JsonMapper;
import com.todo.app.scheduler.model.Task;
import com.todo.app.scheduler.model.User;
import com.todo.app.scheduler.repository.TaskRepository;
import com.todo.app.scheduler.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessagingService {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<@NonNull String, @NonNull String> kafkaTemplate;
    private final Gson gson;
    private final JsonMapper jsonMapper;
    private final MessageCreator messageCreator;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    public void sendMessageToUser() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
            List<Task> completedTasks = taskRepository
                    .getTasksByCreatedAtBetweenAndIsCompleted(yesterday, LocalDateTime.now(), true);
            List<Task> incompletedTasks = taskRepository
                    .getTasksByCreatedAtBetweenAndIsCompleted(yesterday, LocalDateTime.now(), false);
            processTasksUsers(true, completedTasks);
            processTasksUsers(false, incompletedTasks);
        }
    }

    private void processTasksUsers(boolean isCompleted, List<Task> tasks) {
        if (tasks != null && !tasks.isEmpty()) {
            KafkaMessageDto dtoMessage = messageCreator.create(isCompleted, tasks);
            String jsomMessage = jsonMapper.toJson(gson, dtoMessage);
            sendMessageByKafka(jsomMessage);
        }
    }

    private void sendMessageByKafka(String jsonMessage) {
        kafkaTemplate.send(kafkaProperties.topicName(), jsonMessage)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        throw new KafkaSendMessageException("Exception during sending kafka message");
                    }
                });
    }
}

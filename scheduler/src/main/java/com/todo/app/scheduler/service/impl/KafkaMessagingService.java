package com.todo.app.scheduler.service.impl;

import com.todo.app.scheduler.model.Task;
import com.todo.app.scheduler.model.User;
import com.todo.app.scheduler.repository.TaskRepository;
import com.todo.app.scheduler.service.MessageCreator;
import com.todo.app.scheduler.service.MessageProcessor;
import com.todo.app.scheduler.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessagingService implements MessageProcessor {

    private final MessageSender messageSender;
    private final MessageCreator messageCreator;
    private final TaskRepository taskRepository;

    @Override
    @Scheduled(cron = "${spring.application.scheduler.crone}", zone = "${spring.application.scheduler.zone}")
    public void processMessage() {
        List<Task> tasksPerDay = getTasksPerDay();
        Map<User, Map<Boolean, List<Task>>> tasksByUser = tasksPerDay.stream()
                .collect(Collectors.groupingBy(
                        Task::getUser,
                        Collectors.groupingBy(Task::isCompleted)
                ));
        tasksByUser.forEach((user, map) -> {
            Optional.ofNullable(map.get(true))
                    .map(tasks ->
                        messageCreator.create(true, tasks, user))
                    .ifPresent(messageSender::sendMessage);
            Optional.ofNullable(map.get(false))
                    .map(tasks -> messageCreator.create(false, tasks, user))
                    .ifPresent(messageSender::sendMessage);
        });
    }

    private List<Task> getTasksPerDay() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findAllByCreatedAtBetweenWithUser(yesterday, now);
    }
}
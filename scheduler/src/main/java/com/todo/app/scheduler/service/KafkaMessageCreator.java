package com.todo.app.scheduler.service;

import com.todo.app.scheduler.dto.KafkaMessageDto;
import com.todo.app.scheduler.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KafkaMessageCreator implements MessageCreator {

    private static final String INCOMPLETED_TASK = "You have %d incompleted tasks: %s";
    private static final String COMPLETED_TASK = "You have %d completed tasks: %s";

    @Override
    public KafkaMessageDto create(boolean isCompleted, List<Task> tasks) {
        return isCompleted
                ? new KafkaMessageDto(
                createMessageByTemplate(COMPLETED_TASK, tasks)
        )
                :
                new KafkaMessageDto(
                        createMessageByTemplate(INCOMPLETED_TASK, tasks)
                );
    }

    private String createMessageByTemplate(String template, List<Task> tasks) {
        int countTasks = tasks.size();
        String titles = tasks.stream()
                .map(Task::getTitle)
                .limit(5)
                .collect(Collectors.joining(", "));
        return String.format(template, countTasks, titles);
    }
}

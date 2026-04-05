package com.todo.app.scheduler.service;

import com.todo.app.scheduler.dto.KafkaMessageDto;
import com.todo.app.scheduler.model.Task;

import java.util.List;

public interface MessageCreator {

    KafkaMessageDto create(boolean isCompleted, List<Task> tasks);
}

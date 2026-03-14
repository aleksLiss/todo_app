package com.todo.app.todo_app.mapper;

import com.todo.app.todo_app.dto.CreateTaskDto;
import com.todo.app.todo_app.dto.TaskDto;
import com.todo.app.todo_app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskDto toTaskDto(Task task);

    Task toTask(CreateTaskDto taskDto);

    Task toTask(TaskDto taskDto);


}

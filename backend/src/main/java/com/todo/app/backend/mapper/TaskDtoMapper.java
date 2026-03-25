package com.todo.app.backend.mapper;

import com.todo.app.backend.dto.task.UpdateTaskDto;
import com.todo.app.backend.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskDtoMapper {

    UpdateTaskDto taskDtoToUpdateTaskDto(Task task);
}

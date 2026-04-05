package com.todo.app.scheduler.repository;

import com.todo.app.scheduler.model.Task;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<@NonNull Task, @NonNull UUID> {

    List<Task> getAllByIsCompletedIsFalse(boolean isCompleted);

    List<Task> getTasksByCreatedAt(LocalDateTime createdAt);

    List<Task> getTasksByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    List<Task> getTasksByCreatedAtBetweenAndIsCompleted(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, boolean isCompleted);
}

package com.todo.app.backend.repository;

import com.todo.app.backend.dto.task.UpdateTaskDto;
import com.todo.app.backend.model.Task;
import com.todo.app.backend.model.User;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<@NonNull Task, @NonNull UUID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Task t WHERE t.id = :taskId")
    void deleteTaskId(@NonNull UUID taskId);

    List<UpdateTaskDto> findAllByCreatedBy(User createdBy);
}

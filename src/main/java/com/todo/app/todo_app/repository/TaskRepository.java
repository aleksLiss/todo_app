package com.todo.app.todo_app.repository;

import com.todo.app.todo_app.model.Task;
import com.todo.app.todo_app.model.User;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<@NonNull Task, @NonNull UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.title = :newTitle, t.description = :desc WHERE t.title = :oldTitle AND t.createdBy = :user")
    void updateTitleAndDescription(
            @Param("user") User user,
            @Param("oldTitle") String oldTitle,
            @Param("newTitle") String newTitle,
            @Param("desc") String desc);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.isCompleted = true, t.completedAt = CURRENT_TIMESTAMP WHERE t.createdBy = :user")
    void completedTask(@Param("user") User user);

    @Modifying
    @Transactional
    void deleteTaskByTitleAndCreatedBy(String title, User createdBy);

    List<Task> findAllByCreatedBy(User createdBy);

    @Modifying
    @Transactional
    void deleteAllByCreatedBy(User createdBy);

    Optional<Task> getTasksByTitleAndCreatedBy(String title, User createdBy);
}

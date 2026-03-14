package com.todo.app.todo_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "task",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_task_title_created_by",
                        columnNames = {"title", "created_by_id"})
        })
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "Title must be not empty")
    @Length(min = 1, max = 100, message = "Title mu be length from 1 to 100")
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Length(max = 255, message = "Description max length = 255")
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;
    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;
    @Column(name = "completed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime completedAt;

}

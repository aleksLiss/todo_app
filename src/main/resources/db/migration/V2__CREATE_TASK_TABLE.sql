CREATE TABLE task
(
    id            UUID    NOT NULL,
    title         VARCHAR(100) NOT NULL,
    description   VARCHAR(255),
    created_by_id UUID,
    is_completed  BOOLEAN NOT NULL,
    completed_at  TIMESTAMP,
    CONSTRAINT pk_task PRIMARY KEY (id),
    CONSTRAINT FK_TASK_ON_CREATED_BY FOREIGN KEY (created_by_id) REFERENCES "user" (id)
);
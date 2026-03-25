CREATE TABLE tasks
(
    id           UUID                        NOT NULL,
    title        VARCHAR(255)                NOT NULL,
    description  VARCHAR(255),
    user_id      UUID                        NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    completed_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tasks PRIMARY KEY (id),
    CONSTRAINT FK_TASKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
);
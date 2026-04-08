CREATE TABLE users
(
    id       UUID         NOT NULL,
    email    VARCHAR(30)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);
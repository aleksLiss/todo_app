CREATE TABLE "users"
(
    id       UUID NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(20) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);
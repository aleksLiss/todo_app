package com.todo.app;

import org.springframework.boot.SpringApplication;

public class TestTodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.from(TodoAppApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

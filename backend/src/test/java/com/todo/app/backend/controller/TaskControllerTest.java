package com.todo.app.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.backend.dto.task.SaveTaskDto;
import com.todo.app.backend.dto.task.UpdateTaskDto;
import com.todo.app.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.todo.app.backend.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @MockitoBean
    private TaskService taskService;
    private UserPrincipal userPrincipal;
    private SaveTaskDto saveTaskDto;
    private static final String URL_API_TASKS = "/api/tasks";
    private UpdateTaskDto updateTaskDto;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:16"));

    @Container
    @ServiceConnection
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeEach
    public void setUp() {
        String email = "aleks@gmail.com";
        String password = "123123123";
        String title = "title";
        String description = "description";
        UUID uuid = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(uuid, email, password);
        saveTaskDto = new SaveTaskDto(title, description);
        updateTaskDto = new UpdateTaskDto(
                taskId,
                "title",
                "description",
                false,
                null,
                null
        );
    }

    @Test
    public void whenSaveTaskThenOk() throws Exception {
        doNothing().when(taskService).save(any(SaveTaskDto.class), any(UserPrincipal.class));
        mockMvc.perform(post(URL_API_TASKS)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveTaskDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenGetListTasksByUserThenOk() throws Exception {
        when(taskService.getListTasksByUser(userPrincipal)).thenReturn(List.of(updateTaskDto));
        mockMvc.perform(get(URL_API_TASKS)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[0].description").value("description"));
    }

    @Test
    public void whenUpdateTaskThenOk() throws Exception {
        doNothing().when(taskService).save(any(SaveTaskDto.class), any(UserPrincipal.class));
        mockMvc.perform(post(URL_API_TASKS)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveTaskDto)))
                .andExpect(status().isCreated());
        UpdateTaskDto updatedTask = new UpdateTaskDto(
                updateTaskDto.id(),
                "new_title",
                "new_description",
                true,
                null,
                null
        );
        when(taskService.updateTaskById(any(UUID.class), any(UpdateTaskDto.class))).thenReturn(updatedTask);
        mockMvc.perform(put(URL_API_TASKS + "/" + updateTaskDto.id())
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedTask.title()));
    }

    @Test
    public void whenDeleteTaskThenOk() throws Exception {
        doNothing().when(taskService).save(any(SaveTaskDto.class), any(UserPrincipal.class));
        mockMvc.perform(post(URL_API_TASKS)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveTaskDto)))
                .andExpect(status().isCreated());
        doNothing().when(taskService).deleteTaskById(any(UUID.class));
        mockMvc.perform(delete(URL_API_TASKS + "/" + updateTaskDto.id())
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        when(taskService.getListTasksByUser(userPrincipal)).thenReturn(List.of());
        mockMvc.perform(get(URL_API_TASKS)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
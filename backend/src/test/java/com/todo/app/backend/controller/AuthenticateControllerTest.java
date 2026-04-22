package com.todo.app.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.backend.dto.user.LoginUserDto;
import com.todo.app.backend.exception.user.UserNotFoundException;
import com.todo.app.backend.security.UserPrincipal;
import com.todo.app.backend.service.JwtService;
import com.todo.app.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticateControllerTest {

    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    private LoginUserDto loginUserDto;
    private UserPrincipal userPrincipal;
    private String token;
    private static final String URL_AUTH_LOGIN = "/api/auth/login";

    @Container
    @ServiceConnection
    static PostgreSQLContainer container = new PostgreSQLContainer(DockerImageName.parse("postgres:16"));

    @Container
    @ServiceConnection
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeEach
    public void setup() {
        String email = "aleks@gmail.com";
        String password = "123123123";
        token = "test-token";
        UUID uuid = UUID.randomUUID();
        loginUserDto = new LoginUserDto(email, password);
        userPrincipal = new UserPrincipal(uuid, email, password);
    }

    @Test
    public void whenEmailIncorrectThenThrowUserNotFoundException() throws Exception {
        when(userService.getUserByEmail(anyString())).thenThrow(new UserNotFoundException());
        mockMvc.perform(MockMvcRequestBuilders.post(URL_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginUserDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUserExistsThenReturnUser() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(userPrincipal);
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(token);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginUserDto)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, token));
    }
}
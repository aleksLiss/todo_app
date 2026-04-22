package com.todo.app.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.backend.dto.user.GetUserResponseDto;
import com.todo.app.backend.dto.user.SignUpUserDto;
import com.todo.app.backend.exception.user.UserAlreadyExists;
import com.todo.app.backend.repository.UserRepository;
import com.todo.app.backend.security.UserPrincipal;
import com.todo.app.backend.service.JwtService;
import com.todo.app.backend.service.SenderService;
import com.todo.app.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
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
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SenderService senderService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtService jwtService;
    private SignUpUserDto signUpUserDto;
    private String token;
    private UserPrincipal userPrincipal;
    private GetUserResponseDto getUserResponseDto;
    private static final String URL_API_USER = "/api/user";

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16"));

    @Container
    @ServiceConnection
    static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeEach
    public void setUp() {
        String email = "aleks@gmail.com";
        String password = "123123123";
        signUpUserDto = new SignUpUserDto(email, password, password);
        token = "test-token";
        UUID uuid = UUID.randomUUID();
        userPrincipal = new UserPrincipal(uuid, email, password);
        getUserResponseDto = new GetUserResponseDto(uuid, email);
    }

    @AfterEach
    public void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldSignUpUserThenOk() throws Exception {
        when(userService.save(any(SignUpUserDto.class))).thenReturn(userPrincipal);
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(token);
        mockMvc.perform(post(URL_API_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpUserDto)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, token));
    }

    @Test
    public void whenSignUpTheUserAlreadyExists() throws Exception {
        when(userService.save(any(SignUpUserDto.class))).thenThrow(new UserAlreadyExists());
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpUserDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenGetUserThenOk() throws Exception {
        mockMvc.perform(get(URL_API_USER)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getUserResponseDto.id().toString()))
                .andExpect(jsonPath("$.email").value(getUserResponseDto.email()));
    }

    @Test
    public void whenGetUserThenThrowForbiddenException() throws Exception {
        mockMvc.perform(get(URL_API_USER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
package com.todo.app.backend.service;

import com.todo.app.backend.config.JwtProperties;
import com.todo.app.backend.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private UserPrincipal userPrincipal;
    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        String secretString = "Pqwe+ieqwe113RLDoeqwe123eqweL5L1z7jujb2WFzT6Wfe11LQWEAY=";
        UUID uuid = UUID.randomUUID();
        String email = "aleks@gmail.com";
        String password = "password";
        userPrincipal = new UserPrincipal(uuid, email, password);
        when(jwtProperties.secretKey()).thenReturn(secretString);
        when(jwtProperties.lifetimeToken()).thenReturn(Duration.ofMinutes(30));
    }

    @Test
    public void whenGeneratedTokenThenSuccess() {
        String generatedToken = jwtService.generateToken(userPrincipal);
        assertThat(generatedToken).isNotNull();
    }

    @Test
    public void whenValidateTokenThenSuccess() {
        String generatedToken = jwtService.generateToken(userPrincipal);
        assertThat(generatedToken).isNotNull();
        Claims claims = jwtService.validateToken(generatedToken);
        assertThat(claims).isNotNull();
        long expiredAt = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        assertThat(expiredAt).isEqualTo(Duration.ofMinutes(30).toMillis());
    }
}
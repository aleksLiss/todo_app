package com.todo.app.backend.repository;

import com.todo.app.backend.dto.RequestUserDto;
import com.todo.app.backend.model.User;
import com.todo.app.backend.security.UserPrincipal;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<@NonNull User, @NonNull UUID> {

    Optional<User> getUserByEmail(String email);
}

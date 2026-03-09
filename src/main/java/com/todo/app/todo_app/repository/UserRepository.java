package com.todo.app.todo_app.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import com.todo.app.todo_app.model.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull UUID> {

    User findUserByEmail(String email);

    boolean existsUserByEmail(String email);
}

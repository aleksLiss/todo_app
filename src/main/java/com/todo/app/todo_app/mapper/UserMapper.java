package com.todo.app.todo_app.mapper;

import com.todo.app.todo_app.dto.request.SaveUserDto;
import com.todo.app.todo_app.dto.UserDetailsImpl;
import com.todo.app.todo_app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "email", target = "email")
    User toUser(SaveUserDto userDto);

    UserDetailsImpl toUserDetails(User user);
}

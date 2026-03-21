package com.todo.app.backend.mapper;

import com.todo.app.backend.dto.GetUserResponseDto;
import com.todo.app.backend.model.User;
import com.todo.app.backend.security.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseDtoMapper {

    GetUserResponseDto toGetUserResponseDto(UserPrincipal userPrincipal);
}

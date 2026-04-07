package com.todo.app.email_sender.mapper;

import com.google.gson.Gson;
import com.todo.app.email_sender.dto.KafkaMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JsonMapper {

    default String toJson(Gson  gson, Object obj) {
        return obj != null ? gson.toJson(obj) : null;
    }

    default KafkaMessageDto fromJson(Gson gson, String json) {
        return json != null ? gson.fromJson(json, KafkaMessageDto.class) : null;
    }
}

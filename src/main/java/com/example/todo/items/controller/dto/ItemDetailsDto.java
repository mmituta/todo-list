package com.example.todo.items.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ItemDetailsDto {
    private UUID id;
    private String description;
    private OffsetDateTime dueDateTime;
    private OffsetDateTime created;
    private OffsetDateTime finished;
    private StatusDto status;
}

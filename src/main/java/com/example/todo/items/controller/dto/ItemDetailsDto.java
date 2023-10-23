package com.example.todo.items.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemDetailsDto {
    private UUID id;
    private String description;
    private OffsetDateTime dueDateTime;
    private OffsetDateTime created;
    private StatusDto status;
}

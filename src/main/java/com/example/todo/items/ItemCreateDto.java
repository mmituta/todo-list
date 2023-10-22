package com.example.todo.items;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ItemCreateDto {
    private String description;

    private OffsetDateTime dueDateTime;
}

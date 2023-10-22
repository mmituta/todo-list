package com.example.todo.items;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ItemCreateDto {

    @NotBlank
    private String description;

    @NotNull
    private OffsetDateTime dueDateTime;
}

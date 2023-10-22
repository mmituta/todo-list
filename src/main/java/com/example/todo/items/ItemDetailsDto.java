package com.example.todo.items;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemDetailsDto {
    private UUID id;
    private String description;
}

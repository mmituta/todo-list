package com.example.todo.items.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemUpdateDto {
    private StatusDto status;
    private String description;
}

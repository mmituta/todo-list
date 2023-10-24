package com.example.todo.items.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemUpdateDto {
    private StatusDto status;
    private String description;
}

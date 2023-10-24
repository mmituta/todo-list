package com.example.todo.items.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemUpdateDto {
    private StatusUpdateDto status;
    private String description;
}

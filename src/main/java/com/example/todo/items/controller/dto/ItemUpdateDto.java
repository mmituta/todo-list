package com.example.todo.items.controller.dto;

import com.example.todo.items.controller.validation.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemUpdateDto {
    private StatusUpdateDto status;

    @NullOrNotBlank
    private String description;
}

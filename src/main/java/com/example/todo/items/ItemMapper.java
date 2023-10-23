package com.example.todo.items;

import com.example.todo.items.controller.dto.ItemCreateDto;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import com.example.todo.items.controller.dto.StatusDto;
import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.repository.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemEntity map(ItemCreateDto dto);

    ItemDetailsDto map(ItemEntity entity);

    Status map(StatusDto status);
}

package com.example.todo.items;

import com.example.todo.items.controller.ItemCreateDto;
import com.example.todo.items.controller.ItemDetailsDto;
import com.example.todo.items.repository.ItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemEntity map(ItemCreateDto dto);

    ItemDetailsDto map(ItemEntity entity);
}

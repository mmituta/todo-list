package com.example.todo.items;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemEntity map(ItemCreateDto dto);

    ItemDetailsDto map(ItemEntity entity);
}

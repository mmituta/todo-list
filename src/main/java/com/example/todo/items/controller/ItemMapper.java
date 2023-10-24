package com.example.todo.items.controller;

import com.example.todo.items.CurrentDateTimeProvider;
import com.example.todo.items.controller.dto.ItemCreateDto;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import com.example.todo.items.controller.dto.StatusDto;
import com.example.todo.items.controller.dto.StatusUpdateDto;
import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.repository.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Autowired
    protected CurrentDateTimeProvider currentDateTimeProvider;

    public abstract ItemEntity map(ItemCreateDto dto);

    @Mappings({
            @Mapping(target="status", source="item")
    })
    public abstract ItemDetailsDto map(ItemEntity item);

    public abstract Status map(StatusUpdateDto status);

    public abstract StatusDto map(Status status);
    public StatusDto mapStatus(ItemEntity entity){
        if(entity.isPastDue(currentDateTimeProvider.now())){
            return StatusDto.PAST_DUE;
        }
        return this.map(entity.getStatus());
    }
}

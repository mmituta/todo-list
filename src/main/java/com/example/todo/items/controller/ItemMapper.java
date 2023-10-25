package com.example.todo.items.controller;

import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.model.ItemUpdate;
import com.example.todo.items.controller.dto.*;
import com.example.todo.items.model.Item;
import com.example.todo.items.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Autowired
    protected CurrentDateTimeProvider currentDateTimeProvider;

    public abstract Item map(ItemCreateDto dto);

    @Mappings({
            @Mapping(target="status", source="item")
    })
    public abstract ItemDetailsDto map(Item item);

    public abstract ItemUpdate map(ItemUpdateDto dto);

    public abstract Status map(StatusUpdateDto status);

    public abstract StatusDto map(Status status);
    public StatusDto mapStatus(Item entity){
        if(entity.isPastDue(currentDateTimeProvider.now())){
            return StatusDto.PAST_DUE;
        }
        return this.map(entity.getStatus());
    }
}

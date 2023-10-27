package com.example.todo.items.controller;

import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.model.ItemUpdate;
import com.example.todo.items.controller.dto.*;
import com.example.todo.items.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ItemMapper {

    @Autowired
    protected CurrentDateTimeProvider currentDateTimeProvider;

    public abstract Item map(ItemCreateDto dto);

    @Mappings({@Mapping(target = "status", source = "item")})
    public abstract ItemDetailsDto map(Item item);

    @Mappings({@Mapping(target = "done", source = "status")})
    public abstract ItemUpdate map(ItemUpdateDto dto);

    public boolean map(StatusUpdateDto status) {
        return status == StatusUpdateDto.DONE;
    }

    public StatusDto mapStatus(Item entity) {
        if (entity.isPastDue(currentDateTimeProvider.now())) {
            return StatusDto.PAST_DUE;
        }
        return entity.isDone() ? StatusDto.DONE : StatusDto.NOT_DONE;
    }
}

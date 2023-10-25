package com.example.todo.items;

import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.repository.Status;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public abstract class ItemUpdater {
    @Autowired
    protected CurrentDateTimeProvider currentDateTimeProvider;

    @Mappings({
            @Mapping(target="finished", source="status")
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract ItemEntity updateItem(ItemUpdate update, @MappingTarget ItemEntity item);

    protected OffsetDateTime map(Status status ){
        return status == Status.DONE? this.currentDateTimeProvider.now() : null;
    }
}
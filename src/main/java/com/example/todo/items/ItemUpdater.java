package com.example.todo.items;

import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.model.Item;
import com.example.todo.items.model.ItemUpdate;
import com.example.todo.items.model.Status;
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
    public abstract Item updateItem(ItemUpdate update, @MappingTarget Item item);

    protected OffsetDateTime map(Status status ){
        return status == Status.DONE? this.currentDateTimeProvider.now() : null;
    }
}

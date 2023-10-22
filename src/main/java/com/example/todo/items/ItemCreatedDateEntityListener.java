package com.example.todo.items;

import jakarta.persistence.PrePersist;
import org.springframework.stereotype.Component;

@Component
public class ItemCreatedDateEntityListener {
    private final CurrentDateTimeProvider currentDateTimeProvider;

    public ItemCreatedDateEntityListener(CurrentDateTimeProvider currentDateTimeProvider) {
        this.currentDateTimeProvider = currentDateTimeProvider;
    }

    @PrePersist
    public void prePersist(ItemEntity itemEntity){
        itemEntity.setCreated(this.currentDateTimeProvider.now());
    }
}

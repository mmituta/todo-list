package com.example.todo.items.repository;

import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.model.Item;
import jakarta.persistence.PrePersist;
import org.springframework.stereotype.Component;

@Component
public class ItemCreatedDateEntityListener {
    private final CurrentDateTimeProvider currentDateTimeProvider;

    public ItemCreatedDateEntityListener(CurrentDateTimeProvider currentDateTimeProvider) {
        this.currentDateTimeProvider = currentDateTimeProvider;
    }

    @PrePersist
    public void prePersist(Item item) {
        item.setCreated(this.currentDateTimeProvider.now());
    }
}

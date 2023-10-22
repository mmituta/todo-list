package com.example.todo.items;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    private OffsetDateTime dueDateTime;

    private OffsetDateTime created;

    @PrePersist
    void onCreate(){
        this.created = OffsetDateTime.now();
    }
}

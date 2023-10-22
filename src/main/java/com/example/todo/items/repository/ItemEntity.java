package com.example.todo.items.repository;

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
@EntityListeners(ItemCreatedDateEntityListener.class)
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    private OffsetDateTime dueDateTime;

    private OffsetDateTime created;

    private Status status = Status.NOT_DONE;
}

package com.example.todo.items.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ItemCreatedDateEntityListener.class)
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    private OffsetDateTime dueDateTime;

    private OffsetDateTime created;

    private OffsetDateTime finished;

    private Status status = Status.NOT_DONE;


}

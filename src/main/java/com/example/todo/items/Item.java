package com.example.todo.items;

import com.example.todo.items.repository.ItemCreatedDateEntityListener;
import com.example.todo.items.repository.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ItemCreatedDateEntityListener.class)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String description;

    @NotNull
    private OffsetDateTime dueDateTime;

    private OffsetDateTime created;

    private OffsetDateTime finished;

    private Status status = Status.NOT_DONE;


    public boolean isPastDue(OffsetDateTime now) {
        return !this.dueDateTime.isAfter(now);
    }
}

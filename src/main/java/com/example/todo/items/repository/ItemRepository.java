package com.example.todo.items.repository;

import com.example.todo.items.model.Item;
import com.example.todo.items.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {
    Iterable<Item> findAllByStatusAndDueDateTimeAfter(Status status, OffsetDateTime currentTime);

    Iterable<Item> findAllByDueDateTimeLessThanEqual(OffsetDateTime currentTime);
}

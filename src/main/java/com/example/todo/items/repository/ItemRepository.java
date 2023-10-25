package com.example.todo.items.repository;

import com.example.todo.items.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {
    Iterable<Item> findAllByStatus(Status status);
}

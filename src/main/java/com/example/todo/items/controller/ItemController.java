package com.example.todo.items.controller;


import com.example.todo.items.ItemService;
import com.example.todo.items.controller.dto.ItemCreateDto;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import com.example.todo.items.controller.dto.ItemUpdateDto;
import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.ItemMapper;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("items")
public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ResponseEntity<ItemDetailsDto> create(@Valid @RequestBody final ItemCreateDto item) {
        ItemEntity saved = this.itemService.create(this.itemMapper.map(item));

        return ResponseEntity.status(201).body(this.itemMapper.map(saved));
    }

    @GetMapping
    public ResponseEntity<Collection<ItemDetailsDto>> getAllItems() {
        Iterable<ItemEntity> items =this.itemService.findAll();
        return ResponseEntity.ok(StreamSupport.stream(items.spliterator(), false).map(this.itemMapper::map).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> getDetails(@PathVariable UUID id) {
        Optional<ItemEntity> itemEntity = this.itemService.getDetails(id);
        return itemEntity.map(entity -> ResponseEntity.ok(this.itemMapper.map(entity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> updateItem(@PathVariable UUID id, @RequestBody final ItemUpdateDto item) {
        Optional<ItemEntity> itemEntity = this.itemService.update(id, item.getDescription(), this.itemMapper.map(item.getStatus()));
        if (itemEntity.isPresent()) {
            return ResponseEntity.ok(this.itemMapper.map(itemEntity.get()));
        }
        return ResponseEntity.notFound().build();
    }
}

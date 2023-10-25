package com.example.todo.items.controller;


import com.example.todo.items.ItemPastDueException;
import com.example.todo.items.ItemService;
import com.example.todo.items.controller.dto.ItemCreateDto;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import com.example.todo.items.controller.dto.ItemUpdateDto;
import com.example.todo.items.controller.dto.StatusUpdateDto;
import com.example.todo.items.repository.ItemEntity;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Collection<ItemDetailsDto>> getAllItems(@RequestParam(required = false) StatusUpdateDto status) {
        Iterable<ItemEntity> items = findItems(status);
        return ResponseEntity.ok(StreamSupport.stream(items.spliterator(), false).map(this.itemMapper::map).toList());
    }

    private Iterable<ItemEntity> findItems(StatusUpdateDto status) {
        if (status == null) {
            return this.itemService.findAll();
        }

        return this.itemService.findWithStatus(this.itemMapper.map(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> getDetails(@PathVariable UUID id) {
        Optional<ItemEntity> itemEntity = this.itemService.getDetails(id);
        return itemEntity.map(entity -> ResponseEntity.ok(this.itemMapper.map(entity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> updateItem(@PathVariable UUID id, @Valid @RequestBody final ItemUpdateDto item) throws ItemPastDueException {
        Optional<ItemEntity> itemEntity = this.itemService.update(id, item.getDescription(), this.itemMapper.map(item.getStatus()));
        return itemEntity.map(entity -> ResponseEntity.ok(this.itemMapper.map(entity))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

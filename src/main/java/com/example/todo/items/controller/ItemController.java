package com.example.todo.items.controller;


import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.ItemMapper;
import com.example.todo.items.repository.ItemRepository;
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

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemController(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public  ResponseEntity<ItemDetailsDto> create(@Valid @RequestBody final ItemCreateDto item){
        ItemEntity saved = this.itemRepository.save(this.itemMapper.map(item));

        return ResponseEntity.status(201).body(this.itemMapper.map(saved));
    }
    @GetMapping
    public ResponseEntity<Collection<ItemDetailsDto>> getAllItems(){
        Iterable<ItemEntity> items = this.itemRepository.findAll();
        return ResponseEntity.ok(StreamSupport.stream( items.spliterator(), false).map(this.itemMapper::map).toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> getDetails(@PathVariable UUID id){
        Optional<ItemEntity> itemEntity = this.itemRepository.findById(id);
        return itemEntity.map(entity -> ResponseEntity.ok(this.itemMapper.map(entity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

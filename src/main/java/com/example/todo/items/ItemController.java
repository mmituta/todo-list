package com.example.todo.items;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("items")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PostMapping
    public  ResponseEntity<ItemDetailsDto> create(@Valid @RequestBody final ItemCreateDto item){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setDescription(item.getDescription());
        itemEntity.setDueDateTime(item.getDueDateTime());
        ItemEntity saved = this.itemRepository.save(itemEntity);

        return ResponseEntity.status(201).body(new ItemDetailsDto(saved.getId(), saved.getDescription(), saved.getDueDateTime()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> getDetails(@PathVariable UUID id){
        Optional<ItemEntity> itemEntity = this.itemRepository.findById(id);
        return itemEntity.map(entity -> ResponseEntity.ok(new ItemDetailsDto(entity.getId(), entity.getDescription(), entity.getDueDateTime()))).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

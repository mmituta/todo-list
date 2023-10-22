package com.example.todo.items;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public  ResponseEntity<ItemDetailsDto> create(@RequestBody final ItemCreateDto item){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setDescription(item.getDescription());
        ItemEntity saved = this.itemRepository.save(itemEntity);

        return ResponseEntity.status(201).body(new ItemDetailsDto(saved.getId(), saved.getDescription()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> getDetails(@PathVariable UUID id){
        Optional<ItemEntity> itemEntity = this.itemRepository.findById(id);
        return itemEntity.map(entity -> ResponseEntity.ok(new ItemDetailsDto(entity.getId(), entity.getDescription()))).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

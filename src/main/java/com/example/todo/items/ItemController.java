package com.example.todo.items;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

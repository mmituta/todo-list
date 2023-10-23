package com.example.todo.items;

import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.repository.ItemRepository;
import com.example.todo.items.repository.Status;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemEntity create(ItemEntity item){
        return this.itemRepository.save(item);
    }

    public Iterable<ItemEntity> findAll(){
        return this.itemRepository.findAll();
    }

    @Transactional
    public Optional<ItemEntity> update(UUID id, String description, Status status){
        Optional<ItemEntity> itemEntity = this.itemRepository.findById(id);
        if (itemEntity.isPresent()) {
            ItemEntity entity = itemEntity.get();
            if (StringUtils.isNotBlank(description)) {
                entity.setDescription(description);
            }
            if (status != null) {
                entity.setStatus(status);
            }
            return Optional.of(entity);

        }
        return itemEntity;
    }

    public Optional<ItemEntity> getDetails(UUID id){
       return this.itemRepository.findById(id);
    }
}

package com.example.todo.items;

import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.repository.ItemRepository;
import com.example.todo.items.repository.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CurrentDateTimeProvider currentDateTimeProvider;

    private final ItemUpdater itemUpdater;

    public ItemService(ItemRepository itemRepository, CurrentDateTimeProvider currentDateTimeProvider, ItemUpdater itemUpdater) {
        this.itemRepository = itemRepository;
        this.currentDateTimeProvider = currentDateTimeProvider;
        this.itemUpdater = itemUpdater;
    }

    public ItemEntity create(ItemEntity item){
        return this.itemRepository.save(item);
    }

    public Iterable<ItemEntity> findAll(){
        return this.itemRepository.findAll();
    }

    @Transactional
    public Optional<ItemEntity> update(UUID id, ItemUpdate update) throws ItemPastDueException {
        Optional<ItemEntity> itemEntity = this.itemRepository.findById(id);
        if (itemEntity.isPresent()) {
            ItemEntity item = itemEntity.get();
            if( item.isPastDue(this.currentDateTimeProvider.now())){
                throw new ItemPastDueException();
            }
            return Optional.of( itemUpdater.updateItem(update, item));

        }
        return itemEntity;
    }

    public Optional<ItemEntity> getDetails(UUID id){
       return this.itemRepository.findById(id);
    }

    public Iterable<ItemEntity> findWithStatus(Status status) {
        return this.itemRepository.findAllByStatus(status);
    }
}

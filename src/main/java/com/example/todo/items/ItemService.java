package com.example.todo.items;

import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.model.Item;
import com.example.todo.items.model.ItemUpdate;
import com.example.todo.items.repository.ItemRepository;
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

    public Item create(Item item) {
        return this.itemRepository.save(item);
    }

    public Iterable<Item> findAll() {
        return this.itemRepository.findAll();
    }

    @Transactional
    public Optional<Item> update(UUID id, ItemUpdate update) throws PastDueItemModificationException {
        Optional<Item> itemEntity = this.itemRepository.findById(id);
        if (itemEntity.isEmpty()) {
            return itemEntity;
        }
        Item item = itemEntity.get();
        if (isPastDue(item)) {
            throw new PastDueItemModificationException();
        }
        return Optional.of(itemUpdater.updateItem(update, item));
    }

    private boolean isPastDue(Item item) {
        return item.isPastDue(this.currentDateTimeProvider.now());
    }

    public Optional<Item> getDetails(UUID id) {
        return this.itemRepository.findById(id);
    }

    public Iterable<Item> findWithStatus(boolean isDone) {
        return this.itemRepository.findAllByDoneAndDueDateTimeAfter(isDone, this.currentDateTimeProvider.now());
    }

    public Iterable<Item> findPastDueItems() {
        return this.itemRepository.findAllByDueDateTimeLessThanEqual(this.currentDateTimeProvider.now());
    }
}

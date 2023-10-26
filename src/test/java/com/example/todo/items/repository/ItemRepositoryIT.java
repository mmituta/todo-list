package com.example.todo.items.repository;

import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class ItemRepositoryIT {
    @MockBean
    private CurrentDateTimeProvider currentDateTimeProvider;
    @Autowired
    private ItemRepository itemRepository;


    @Test
    void shouldSetCreatedDateTimeWhenPersistingAnItem() {
        when(this.currentDateTimeProvider.now()).thenReturn(OffsetDateTime.MIN);

        Item saved = this.itemRepository.save(new Item(null, "desc", OffsetDateTime.MAX, null, null, false));

        assertThat(saved.getCreated()).isEqualTo(OffsetDateTime.MIN);
    }
}

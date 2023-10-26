package com.example.todo.items;

import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.model.Item;
import com.example.todo.items.model.ItemUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemUpdaterTest {
    private static final UUID ANY_UUID = UUID.fromString("952b42f5-ec46-496d-84a1-af0722055e34");

    @Mock
    private CurrentDateTimeProvider currentDateTimeProvider;
    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.parse("2023-10-25T18:00:00Z");

    @InjectMocks
    private ItemUpdaterImpl itemUpdater;
    private Item item;

    @BeforeEach
    void setUpItem() {
        item = new Item(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, null, false);
    }

    @Test
    void shouldUpdateDescriptionIfDescriptionIsNotBlank() {
        Item result = itemUpdater.updateItem(new ItemUpdate("test", null), item);

        assertThat(result.getDescription()).isEqualTo("test");
    }

    @Test
    void shouldNotUpdateDescriptionIfDescriptionIsNull() {
        Item result = itemUpdater.updateItem(new ItemUpdate(null, null), item);

        assertThat(result.getDescription()).isEqualTo(item.getDescription());
    }


    @Test
    void shouldUpdateStatusIfStatusIsNotNull() {
        when(this.currentDateTimeProvider.now()).thenReturn(CURRENT_TIME);

        Item result = itemUpdater.updateItem(new ItemUpdate(null, true), item);

        assertThat(result.isDone()).isTrue();
        assertThat(result.getFinished()).isEqualTo(CURRENT_TIME);
    }

    @Test
    void shouldNotUpdateStatusIfStatusIsNull() {
        Item result = itemUpdater.updateItem(new ItemUpdate(null, null), item);

        assertThat(result.isDone()).isEqualTo(item.isDone());
    }

    @Test
    void shouldUpdateDescriptionAndStatusAtTheSameTime() {
        when(this.currentDateTimeProvider.now()).thenReturn(CURRENT_TIME);
        Item result = itemUpdater.updateItem(new ItemUpdate("test", true), item);

        assertThat(result.isDone()).isTrue();
        assertThat(result.getDescription()).isEqualTo("test");
        assertThat(result.getFinished()).isEqualTo(CURRENT_TIME);
    }

    @Test
    void shouldClearFinishedTimeWhenTheItemIsMarkedAsNotDone() {
        Item doneItem = new Item(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, CURRENT_TIME, true);

        Item result = itemUpdater.updateItem(new ItemUpdate(null, false), doneItem);
        assertThat(result.getFinished()).isNull();
    }

}

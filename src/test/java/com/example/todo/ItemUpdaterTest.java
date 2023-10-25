package com.example.todo;

import com.example.todo.items.CurrentDateTimeProvider;
import com.example.todo.items.ItemUpdate;
import com.example.todo.items.ItemUpdaterImpl;
import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.repository.Status;
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
    private ItemEntity itemEntity;

    @BeforeEach
    void setUpItem(){
        itemEntity = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, null, Status.NOT_DONE);
    }

    @Test
    void shouldUpdateDescriptionIfDescriptionIsNotBlank()  {
        ItemEntity result = itemUpdater.updateItem(new ItemUpdate("test", null), itemEntity);

        assertThat(result.getDescription()).isEqualTo("test");
    }

    @Test
    void shouldNotUpdateDescriptionIfDescriptionIsNull()  {
        ItemEntity result = itemUpdater.updateItem(new ItemUpdate(null, null), itemEntity);

        assertThat(result.getDescription()).isEqualTo(itemEntity.getDescription());
    }


    @Test
    void shouldUpdateStatusIfStatusIsNotNull()  {
        when(this.currentDateTimeProvider.now()).thenReturn(CURRENT_TIME);

        ItemEntity result = itemUpdater.updateItem(new ItemUpdate(null, Status.DONE), itemEntity);

        assertThat(result.getStatus()).isEqualTo(Status.DONE);
        assertThat(result.getFinished()).isEqualTo(CURRENT_TIME);
    }

    @Test
    void shouldNotUpdateStatusIfStatusIsNull() {
        ItemEntity result = itemUpdater.updateItem(new ItemUpdate(null, null), itemEntity);

        assertThat(result.getStatus()).isEqualTo(itemEntity.getStatus());
    }

    @Test
    void shouldUpdateDescriptionAndStatusAtTheSameTime() {
        when(this.currentDateTimeProvider.now()).thenReturn(CURRENT_TIME);
        ItemEntity result = itemUpdater.updateItem(new ItemUpdate("test", Status.DONE), itemEntity);

        assertThat(result.getStatus()).isEqualTo(Status.DONE);
        assertThat(result.getDescription()).isEqualTo("test");
        assertThat(result.getFinished()).isEqualTo(CURRENT_TIME);
    }

    @Test
    void shouldClearFinishedTimeWhenTheItemIsMarkedAsNotDone() {
        ItemEntity doneItem = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, CURRENT_TIME, Status.DONE);

        ItemEntity result = itemUpdater.updateItem(new ItemUpdate(null, Status.NOT_DONE), doneItem);
        assertThat(result.getFinished()).isNull();
    }

}

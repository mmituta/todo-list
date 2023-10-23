package com.example.todo;


import com.example.todo.items.ItemService;
import com.example.todo.items.CurrentDateTimeProvider;
import com.example.todo.items.repository.ItemEntity;
import com.example.todo.items.repository.ItemRepository;
import com.example.todo.items.repository.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    private static final UUID ANY_UUID = UUID.fromString("952b42f5-ec46-496d-84a1-af0722055e34");
    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.parse("2023-10-23T18:00:00Z");
    @Mock
    private ItemRepository repository;

    @Mock
    private CurrentDateTimeProvider currentDateTimeProvider;

    @InjectMocks
    private ItemService service;



    @Test
    void shouldUpdateDescriptionIfDescriptionIsNotBlank(){
        ItemEntity itemEntity = new ItemEntity();
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));

        Optional<ItemEntity> updated = service.update(ANY_UUID, "description", null);

        assertThat(updated).isPresent();
        assertThat(updated.get().getDescription()).isEqualTo("description");
    }

    @Test
    void shouldNotUpdateDescriptionIfDescriptionIsNull(){
        ItemEntity itemEntity = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, null, Status.DONE);
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));

        Optional<ItemEntity> updated = service.update(ANY_UUID, null, null);

        assertThat(updated).isPresent();
        assertThat(updated.get().getDescription()).isEqualTo("old");
    }

    @Test
    void shouldNotUpdateDescriptionIfDescriptionIsBlank(){
        ItemEntity itemEntity = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, null, Status.DONE);

        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));

        Optional<ItemEntity> updated = service.update(ANY_UUID, "  ", null);

        assertThat(updated).isPresent();
        assertThat(updated.get().getDescription()).isEqualTo("old");

    }

    @Test
    void shouldUpdateStatusIfStatusIsNotNull(){
        ItemEntity itemEntity = new ItemEntity();
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));

        Optional<ItemEntity> updated = service.update(ANY_UUID, null, Status.DONE);

        assertThat(updated).isPresent();
        assertThat(updated.get().getStatus()).isEqualTo(Status.DONE);
    }

    @Test
    void shouldNotUpdateStatusIfStatusIsNull(){
        ItemEntity itemEntity = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, null, Status.DONE);
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));

        Optional<ItemEntity> updated = service.update(ANY_UUID, null, null);

        assertThat(updated).isPresent();
        assertThat(updated.get().getStatus()).isEqualTo(Status.DONE);
    }

    @Test
    void shouldUpdateDescriptionAndStatusAtTheSameTime(){
        ItemEntity itemEntity = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, null, Status.DONE);
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));

        Optional<ItemEntity> updated = service.update(ANY_UUID, "new", Status.NOT_DONE);

        assertThat(updated).isPresent();
        assertThat(updated.get().getStatus()).isEqualTo(Status.NOT_DONE);
        assertThat(updated.get().getDescription()).isEqualTo("new");
    }

    @Test
    void shouldNotUpdateIfItemDoesNotExist(){
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.empty());

        assertThat(service.update(ANY_UUID, "new", Status.NOT_DONE)).isEmpty();
    }

    @Test
    void shouldSetTheFinishedTimeWhenItemIsMarkAsDone(){
        ItemEntity itemEntity = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, null, Status.DONE);
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));
        when(this.currentDateTimeProvider.now()).thenReturn(CURRENT_TIME);

        Optional<ItemEntity> updated = service.update(ANY_UUID, "new", Status.DONE);
        assertThat(updated).isPresent();
        assertThat(updated.get().getFinished()).isEqualTo(CURRENT_TIME);
    }

    @Test
    void shouldClearFinishedTimeWhenItemIsMarkAsDone(){
        ItemEntity itemEntity = new ItemEntity(ANY_UUID, "old", OffsetDateTime.MAX, OffsetDateTime.MIN, CURRENT_TIME, Status.DONE);
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(itemEntity));

        Optional<ItemEntity> updated = service.update(ANY_UUID, "new", Status.NOT_DONE);
        assertThat(updated).isPresent();
        assertThat(updated.get().getFinished()).isNull();
    }
}

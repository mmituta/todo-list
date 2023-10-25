package com.example.todo.items;

import com.example.todo.items.repository.ItemRepository;
import com.example.todo.items.repository.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    private static final UUID ANY_UUID = UUID.fromString("952b42f5-ec46-496d-84a1-af0722055e34");
    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.parse("2023-10-23T18:00:00Z");
    public static final OffsetDateTime FUTURE_DATE = CURRENT_TIME.plusDays(1);
    @Mock
    private ItemRepository repository;

    @Mock
    private CurrentDateTimeProvider currentDateTimeProvider;

    @InjectMocks
    private ItemService service;




    @Test
    void shouldThrowExceptionWhenUpdatingItemThatIsPastDue(){
        Item item = new Item(ANY_UUID, "old", FUTURE_DATE, CURRENT_TIME, null, Status.DONE);
        when(this.repository.findById(ANY_UUID)).thenReturn(Optional.of(item));
        when(this.currentDateTimeProvider.now()).thenReturn(FUTURE_DATE.plusDays(1));

        assertThrows(ItemPastDueException.class, ()-> this.service.update(ANY_UUID, new ItemUpdate("description", Status.DONE)));
    }
}

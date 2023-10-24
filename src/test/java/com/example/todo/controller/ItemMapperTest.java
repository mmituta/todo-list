package com.example.todo.controller;

import com.example.todo.items.CurrentDateTimeProvider;
import com.example.todo.items.controller.ItemMapperImpl;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import com.example.todo.items.controller.dto.StatusDto;
import com.example.todo.items.repository.ItemEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {
    private static final OffsetDateTime PAST_TIME = OffsetDateTime.parse("2023-10-23T16:00:00Z");
    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.parse("2023-10-24T14:00:00Z");
    @Mock
    private CurrentDateTimeProvider currentDateTimeProvider;

    @InjectMocks
    private ItemMapperImpl itemMapper;

    @Test
    void shouldMapItemThatIsPastDueWithPastDueStatus(){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setDueDateTime(PAST_TIME);

        when(this.currentDateTimeProvider.now()).thenReturn(CURRENT_TIME);

        ItemDetailsDto dto = itemMapper.map(itemEntity);
        assertThat(dto.getStatus()).isEqualTo(StatusDto.PAST_DUE);
    }

    @Test
    void shouldMapItemThatHasPastDueTimeEqualToCurrentTimeWithPastDueStatus(){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setDueDateTime(CURRENT_TIME);

        when(this.currentDateTimeProvider.now()).thenReturn(CURRENT_TIME);

        ItemDetailsDto dto = itemMapper.map(itemEntity);
        assertThat(dto.getStatus()).isEqualTo(StatusDto.PAST_DUE);
    }
}



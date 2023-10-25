package com.example.todo.items.controller;

import com.example.todo.items.PastDueItemModificationException;
import com.example.todo.items.ItemUpdate;
import com.example.todo.items.ItemService;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import com.example.todo.items.controller.dto.ItemUpdateDto;
import com.example.todo.items.controller.dto.StatusDto;
import com.example.todo.items.controller.dto.StatusUpdateDto;
import com.example.todo.items.Item;
import com.example.todo.items.repository.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class ItemControllerTest {

    private static final UUID ID = UUID.fromString("22515e9d-b9dd-4b81-a774-694f8250aec5");
    private static final String DESCRIPTION = "description";
    @Mock
    private ItemService itemService;

    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemController itemController;

    @Test
    void shouldGetAllTheItemsIfProvidedStatusIsNull(){
        Item firstItem = new Item();
        Item secondItem = new Item();

        ItemDetailsDto firstDto = ItemDetailsDto.builder().description("first").build();
        ItemDetailsDto secondDto = ItemDetailsDto.builder().description("second").build();
        when(this.itemMapper.map(firstItem)).thenReturn(firstDto);
        when(this.itemMapper.map(secondItem)).thenReturn(secondDto);
        when(this.itemService.findAll()).thenReturn(asList(firstItem, secondItem));

        ResponseEntity<Collection<ItemDetailsDto>> result = this.itemController.getAllItems(null);

        assertThat(result.getBody()).containsOnly(firstDto, secondDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    void shouldGetOnlyItemsWithSpecifiedCompletionStatus(){
        Item doneItem = new Item();

        ItemDetailsDto doneDto = ItemDetailsDto.builder().status(StatusDto.DONE).build();
        when(this.itemMapper.map(doneItem)).thenReturn(doneDto);
        when(this.itemMapper.map(StatusUpdateDto.DONE)).thenReturn(Status.DONE);
        when(this.itemService.findWithStatus(Status.DONE)).thenReturn(singleton(doneItem));

        ResponseEntity<Collection<ItemDetailsDto>> result = this.itemController.getAllItems(StatusUpdateDto.DONE);

        assertThat(result.getBody()).containsOnly(doneDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    void shouldUpdateItem() throws PastDueItemModificationException {
        ItemUpdateDto updateDto = ItemUpdateDto.builder().description(DESCRIPTION).status(StatusUpdateDto.DONE).build();
        ItemUpdate update = new ItemUpdate(DESCRIPTION, Status.DONE);
        when(this.itemMapper.map(updateDto)).thenReturn(update);
        Item updateEntity = new Item();
        when(this.itemService.update(ID, update)).thenReturn(Optional.of(updateEntity));
        ItemDetailsDto expectedDto = ItemDetailsDto.builder().id(ID).build();
        when(this.itemMapper.map(updateEntity)).thenReturn(expectedDto);

        ResponseEntity<ItemDetailsDto> result = this.itemController.updateItem(ID, updateDto);

        assertThat(result.getBody()).isEqualTo(expectedDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

    }

    @Test
    void shouldReturnNotFoundResponseIfUpdatedItemDoesNotExist() throws PastDueItemModificationException {
        ItemUpdateDto updateDto = ItemUpdateDto.builder().description(DESCRIPTION).status(StatusUpdateDto.DONE).build();
        ItemUpdate update = new ItemUpdate(DESCRIPTION, Status.DONE);
        when(this.itemMapper.map(updateDto)).thenReturn(update);
        when(this.itemService.update(ID, update)).thenReturn(Optional.empty());

        ResponseEntity<ItemDetailsDto> result = this.itemController.updateItem(ID, updateDto);

        assertThat(result.getBody()).isNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }
}

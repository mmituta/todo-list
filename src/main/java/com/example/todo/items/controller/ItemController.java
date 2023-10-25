package com.example.todo.items.controller;


import com.example.todo.items.PastDueItemModificationException;
import com.example.todo.items.ItemService;
import com.example.todo.items.controller.dto.ItemCreateDto;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import com.example.todo.items.controller.dto.ItemUpdateDto;
import com.example.todo.items.controller.dto.StatusUpdateDto;
import com.example.todo.items.model.Item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("items")
public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @Operation(summary = "Create a new item",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Item was successfully created",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemDetailsDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)}
    )
    @PostMapping
    public ResponseEntity<ItemDetailsDto> create(@Valid @RequestBody final ItemCreateDto item) {
        Item saved = this.itemService.create(this.itemMapper.map(item));

        return ResponseEntity.status(201).body(this.itemMapper.map(saved));
    }

    @Operation(summary = "Allows to get a list of all items. It allows to specify if the returned value should be filtered by status.",
            parameters = {
                @Parameter(name = "status", description = "When set, the endpoint will filter the items by the provided status")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found result for the specified criteria",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemDetailsDto.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect status parameter value", content = @Content)}
    )
    @GetMapping
    public ResponseEntity<Collection<ItemDetailsDto>> getAllItems(@RequestParam(required = false) StatusUpdateDto status) {
        Iterable<Item> items = findItems(status);
        return ResponseEntity.ok(StreamSupport.stream(items.spliterator(), false).map(this.itemMapper::map).toList());
    }

    private Iterable<Item> findItems(StatusUpdateDto status) {
        if (status == null) {
            return this.itemService.findAll();
        }

        return this.itemService.findWithStatus(this.itemMapper.map(status));
    }

    @Operation(summary = "Gets the details of the specified item",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Details of the specified item",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemDetailsDto.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect status parameter value", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No item found", content = @Content)})

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> getDetails(@PathVariable UUID id) {
        Optional<Item> itemEntity = this.itemService.getDetails(id);
        return itemEntity.map(entity -> ResponseEntity.ok(this.itemMapper.map(entity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Updates the specified item with provided information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Details of the updated item",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemDetailsDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No item found", content = @Content)})
    @PatchMapping("/{id}")
    public ResponseEntity<ItemDetailsDto> updateItem(@PathVariable UUID id, @Valid @RequestBody final ItemUpdateDto item) throws PastDueItemModificationException {
        Optional<Item> itemEntity = this.itemService.update(id, this.itemMapper.map(item));
        return itemEntity.map(entity -> ResponseEntity.ok(this.itemMapper.map(entity))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

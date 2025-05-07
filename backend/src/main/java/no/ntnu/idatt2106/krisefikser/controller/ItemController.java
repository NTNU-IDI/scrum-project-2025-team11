package no.ntnu.idatt2106.krisefikser.controller;

import no.ntnu.idatt2106.krisefikser.dto.ItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.ItemResponse;
import no.ntnu.idatt2106.krisefikser.mapper.ItemMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.idatt2106.krisefikser.model.Item;
import no.ntnu.idatt2106.krisefikser.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

/**
 * Controller class for managing items in the system.
 * This class handles HTTP requests related to item operations, such as creating, 
 * updating, deleting, and retrieving items.
 * 
 */
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
@Tag(name = "Item API")
public class ItemController {

    private final ItemService service;
    private final ItemMapper mapper;

    public ItemController(ItemService service, ItemMapper mapper) {
        this.service = service;
        this.mapper  = mapper;
    }

    /**
     * Retrieves a list of all items in the system.
     *
     * @return a list of {@link ItemResponse} objects representing all items
     */
    @GetMapping
    @Operation(summary = "List all items")
    public List<ItemResponse> list() {
        return service.findAll().stream()
                      .map(mapper::toResponse)
                      .toList();
    }

    /**
     * Retrieves an item by its ID.
     *
     * @param id the ID of the item to retrieve
     * @return a {@link ResponseEntity} containing the item if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an item by ID")
    public ResponseEntity<ItemResponse> getOne(@PathVariable Integer id) {
        return service.findById(id)
                      .map(mapper::toResponse)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new item.
     *
     * @param req the request body containing item details
     * @return a {@link ResponseEntity} containing the created item and its location
     */
    @PostMapping
    @Operation(summary = "Create a new item")
    public ResponseEntity<ItemResponse> create(
        @Valid @RequestBody ItemRequest req
    ) {
        Item saved = service.create(mapper.toEntity(req));
        return ResponseEntity
            .created(URI.create("/api/items/" + saved.getId()))
            .body(mapper.toResponse(saved));
    }

    /**
     * Updates an existing item.
     *
     * @param id  the ID of the item to update
     * @param req the request body containing updated item details
     * @return a {@link ResponseEntity} containing the updated item, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item")
    public ResponseEntity<ItemResponse> update(
        @PathVariable Integer id,
        @Valid @RequestBody ItemRequest req
    ) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Item toSave = mapper.toEntity(req);
        toSave.setId(id);
        Item updated = service.update(id, toSave);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    /**
     * Deletes an item by its ID.
     *
     * @param id the ID of the item to delete
     * @return a {@link ResponseEntity} indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
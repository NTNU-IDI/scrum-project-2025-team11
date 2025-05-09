package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.ItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.ItemResponse;
import no.ntnu.idatt2106.krisefikser.mapper.ItemMapper;
import no.ntnu.idatt2106.krisefikser.model.Item;
import no.ntnu.idatt2106.krisefikser.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing items in the global catalog.
 * <p>
 * Provides endpoints to list, retrieve, create, update, and delete item entities.
 */
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Item API", description = "Operations for managing item definitions")
public class ItemController {

    private final ItemService service;
    private final ItemMapper mapper;

    /**
     * Retrieves all items in the system.
     *
     * @return 200 OK with the list of item response DTOs
     */
    @GetMapping
    @Operation(summary = "List all items",
               description = "Fetches a list of all items available in the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    })
    public ResponseEntity<List<ItemResponse>> list() {
        List<ItemResponse> responses = service.findAll().stream()
                                             .map(mapper::toResponse)
                                             .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves a single item by its ID.
     *
     * @param id the unique identifier of the item
     * @return 200 OK with the item data if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID",
               description = "Retrieves the details of an item given its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item found and returned"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<ItemResponse> getOne(
        @Parameter(description = "ID of the item to retrieve", required = true)
        @PathVariable Integer id
    ) {
        return service.findById(id)
                      .map(mapper::toResponse)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new item in the system.
     *
     * @param req the item request payload
     * @return 201 Created with the new item, or 400 Bad Request for invalid data
     */
    @PostMapping
    @Operation(summary = "Create a new item",
               description = "Adds a new item record to the system catalog.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid item data provided")
    })
    public ResponseEntity<ItemResponse> create(
        @Parameter(description = "Item creation payload", required = true)
        @Valid @RequestBody ItemRequest req
    ) {
        Item entity = mapper.toEntity(req);
        Item saved = service.create(entity);
        ItemResponse response = mapper.toResponse(saved);
        URI location = URI.create(String.format("/api/items/%d", saved.getId()));
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Updates an existing item by ID.
     *
     * @param id  the ID of the item to update
     * @param req the updated item data
     * @return 200 OK with the updated item, or 404 Not Found if the item does not exist
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an item",
               description = "Modifies an existing item record identified by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid update data provided"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<ItemResponse> update(
        @Parameter(description = "ID of the item to update", required = true)
        @PathVariable Integer id,
        @Parameter(description = "Updated item payload", required = true)
        @Valid @RequestBody ItemRequest req
    ) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Item entity = mapper.toEntity(req);
        entity.setId(id);
        Item updated = service.update(id, entity);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    /**
     * Deletes an item by its ID.
     *
     * @param id the ID of the item to delete
     * @return 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item",
               description = "Removes the item record identified by its ID from the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID of the item to delete", required = true)
        @PathVariable Integer id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

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

@RestController
@RequestMapping("/api/items")
@Tag(name = "Item API")
public class ItemController {

    private final ItemService service;
    private final ItemMapper mapper;

    public ItemController(ItemService service, ItemMapper mapper) {
        this.service = service;
        this.mapper  = mapper;
    }

    @GetMapping
    @Operation(summary = "List all items")
    public List<ItemResponse> list() {
        return service.findAll().stream()
                      .map(mapper::toResponse)
                      .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an item by ID")
    public ResponseEntity<ItemResponse> getOne(@PathVariable Integer id) {
        return service.findById(id)
                      .map(mapper::toResponse)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


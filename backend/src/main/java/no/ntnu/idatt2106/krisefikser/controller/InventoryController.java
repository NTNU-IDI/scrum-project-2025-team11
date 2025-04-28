package no.ntnu.idatt2106.krisefikser.controller;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.service.InventoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/households/{hhId}/items")
@Tag(name = "Inventory API", description = "Operations for managing household inventory items")
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List all items in a household's inventory")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of items")
    })
    public List<HouseholdItemResponse> list(
        @PathVariable Integer hhId
    ) {
        return service.list(hhId);
    }

    @PostMapping
    @Operation(summary = "Add a new item to a household's inventory")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item successfully added"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Household or Item not found")
    })
    public ResponseEntity<HouseholdItemResponse> add(
        @PathVariable Integer hhId,
        @Valid @RequestBody HouseholdItemRequest req
    ) {
        var created = service.add(hhId, req);
        return ResponseEntity
            .created(URI.create("/api/households/" + hhId + "/items/" + created.getItemId() + "/" + created.getAcquiredDate()))
            .body(created);
    }

    @PutMapping("/{itemId}/{acquiredDate}")
    @Operation(summary = "Update an existing item by purchase date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Inventory entry not found")
    })
    public HouseholdItemResponse update(
        @PathVariable Integer hhId,
        @PathVariable Integer itemId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate acquiredDate,
        @Valid @RequestBody HouseholdItemRequest req
    ) {
        // ensure the request key matches the path
        req.setAcquiredDate(acquiredDate);
        return service.update(hhId, itemId, req);
    }

    @DeleteMapping("/{itemId}/{acquiredDate}")
    @Operation(summary = "Remove a specific purchase of an item")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Item successfully removed"),
        @ApiResponse(responseCode = "404", description = "Inventory entry not found")
    })
    public ResponseEntity<Void> remove(
        @PathVariable Integer hhId,
        @PathVariable Integer itemId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate acquiredDate
    ) {
        service.remove(hhId, itemId, acquiredDate);
        return ResponseEntity.noContent().build();
    }
}

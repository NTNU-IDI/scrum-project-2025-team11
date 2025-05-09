package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.dto.UpsertInventoryRequest;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.InventoryService;
import no.ntnu.idatt2106.krisefikser.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing inventory items within a household.
 * <p>
 * Provides endpoints to list, add, update, remove, and upsert inventory entries.
 */
@RestController
@RequestMapping("/api/households/items")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "jwtCookieAuth")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Tag(name = "Inventory API", description = "Operations for managing household inventory items")
public class InventoryController {

    private final InventoryService service;
    private final UserService userService;

    /**
     * Retrieves inventory items for the authenticated user's household.
     *
     * @param itemId optional filter by a specific item ID
     * @return 200 OK with list of inventory entries
     */
    @Operation(summary = "List household inventory items",
    description = "Returns all inventory entries for the current user's household, optionally filtered by item ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @GetMapping
    public ResponseEntity<List<HouseholdItemResponse>> list(
        @Parameter(description = "ID of the item to filter by", required = false)
        @RequestParam(required = false) Integer itemId
    ) {
        List<HouseholdItemResponse> items = service.list(getHhId(), itemId);
        return ResponseEntity.ok(items);
    }

    /**
     * Adds a new inventory entry to the authenticated user's household.
     *
     * @param req request body containing item details
     * @return 201 Created with the new inventory entry
     */
    @Operation(summary = "Add inventory item",
    description = "Creates a new inventory entry for the current user's household.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Inventory entry created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "Household not found")
    })
    @PostMapping
    public ResponseEntity<HouseholdItemResponse> add(
        @Valid @RequestBody HouseholdItemRequest req
    ) {
        HouseholdItemResponse created = service.add(getHhId(), req);
        URI location = URI.create(String.format(
            "/api/households/items/%d/%s",
            created.getItemId(), created.getAcquiredDate()  
        ));
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Updates an existing inventory entry by item ID and acquired date.
     *
     * @param itemId the item ID
     * @param acquiredDate the acquisition timestamp
     * @param req request body with updated fields
     * @return 200 OK with updated inventory entry
     */
    @Operation(summary = "Update inventory entry",
    description = "Modifies an existing inventory entry identified by item ID and acquisition date.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventory entry updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "Inventory entry not found")
    })
    @PutMapping("/{itemId}/{acquiredDate}")
    public ResponseEntity<HouseholdItemResponse> update(
        @PathVariable Integer itemId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime acquiredDate,
        @Valid @RequestBody HouseholdItemRequest req
    ) {
        req.setItemId(itemId);
        req.setAcquiredDate(acquiredDate);
        HouseholdItemResponse updated = service.update(getHhId(), itemId, req);
        return ResponseEntity.ok(updated);
    }

    /**
     * Removes all inventory entries for a given item in the household.
     *
     * @param itemId the item ID
     * @return 204 No Content if removals succeeded
     */
    @Operation(summary = "Remove all purchases of an item",
    description = "Deletes every inventory entry of the specified item in the household.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "All entries removed"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "No entries found for item")
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeAll(@PathVariable Integer itemId) {
        service.removeAll(getHhId(), itemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Removes a specific inventory entry identified by item ID and acquisition date.
     *
     * @param itemId the item ID
     * @param acquiredDate the acquisition timestamp
     * @return 204 No Content if removal succeeded
     */
    @Operation(summary = "Remove specific purchase entry",
    description = "Deletes a single inventory entry matching item ID and acquisition date.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Entry removed"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "Inventory entry not found")
    })
    @DeleteMapping("/{itemId}/{acquiredDate}")
    public ResponseEntity<Void> removeOne(
        @PathVariable Integer itemId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime acquiredDate
    ) {
        service.remove(getHhId(), itemId, acquiredDate);
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates or links an item and adds it to the household inventory.
     *
     * @param req request body containing upsert details
     * @return 201 Created with the new or linked inventory entry
     */
    @Operation(summary = "Upsert inventory entry",
    description = "Creates a new item if needed and adds it to inventory, or links an existing item.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Upsert operation successful"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "Household not found")
    })
    @PostMapping("/upsert")
    public ResponseEntity<HouseholdItemResponse> upsert(
        @Valid @RequestBody UpsertInventoryRequest req
    ) {
        HouseholdItemResponse created = service.upsert(getHhId(), req);
        URI location = URI.create(String.format(
            "/api/households/items/%d/%s",
            created.getItemId(), created.getAcquiredDate()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Helper to extract household ID from the authenticated user.
     *
     * @return household ID of current user
     */
    private int getHhId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName()).orElseThrow();
        return user.getHouseholdId();
    }
}

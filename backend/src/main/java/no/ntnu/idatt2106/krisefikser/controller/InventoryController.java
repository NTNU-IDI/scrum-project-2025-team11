package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.dto.UpsertInventoryRequest;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.InventoryService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Controller class for managing household inventory items.
 * This class handles HTTP requests related to inventory operations, such as creating, 
 * updating, deleting, and retrieving items in a household's inventory.
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
     * Retrieves a list of all items in a household's inventory.
     *
     * @param hhId the ID of the household
     * @param itemId optional ID of the item to filter by
     * @return a list of {@link HouseholdItemResponse} objects representing all items in the household's inventory
     */
    @GetMapping
    @Operation(summary = "List all items in a household's inventory")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of items")
    })
    public List<HouseholdItemResponse> list(
        @Parameter(description = "ID of the item to filter by", required = false)
        @RequestParam(required = false) Integer itemId
    ) {
        int hhId = getHhId();
        return service.list(hhId);
    }

    /**
     * Adds a new item to a household's inventory.
     *
     * @param hhId the ID of the household
     * @param req the request body containing item details
     * @return a {@link ResponseEntity} containing the created item
     */
    @PostMapping
    @Operation(summary = "Add a new item to a household's inventory")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item successfully added"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Household or Item not found")
    })
    public ResponseEntity<HouseholdItemResponse> add(
        @Valid @RequestBody HouseholdItemRequest req
    ) {
        int hhId = getHhId();
        HouseholdItemResponse created = service.add(hhId, req);
        URI location = URI.create(String.format(
            "/api/households/%d/items/%d/%s",
            hhId,
            created.getItemId(),
            created.getAcquiredDate()
        ));
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Updates an existing item in a household's inventory by purchase date.
     *
     * @param hhId the ID of the household
     * @param itemId the ID of the item to update
     * @param acquiredDate the date and time when the item was acquired
     * @param req the request body containing updated item details
     * @return a {@link HouseholdItemResponse} object representing the updated item
     */
    @PutMapping("/{itemId}/{acquiredDate}")
    @Operation(summary = "Update an existing item by purchase date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Inventory entry not found")
    })
    public HouseholdItemResponse update(
        @PathVariable Integer itemId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime acquiredDate,
        @Valid @RequestBody HouseholdItemRequest req
    ) {
        int hhId = getHhId();
        // ignore any key fields in JSON, use path variables
        req.setItemId(itemId);
        req.setAcquiredDate(acquiredDate);
        return service.update(hhId, itemId, req);
    }

    /**
     * Deletes all purchases of an item in a household's inventory.
     *
     * @param hhId the ID of the household
     * @param itemId the ID of the item to delete
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @DeleteMapping("/{itemId}")
    @Operation(summary = "Remove *all* purchases of an item")
    @ApiResponses({
      @ApiResponse(responseCode = "204", description = "All purchases removed"),
      @ApiResponse(responseCode = "404", description = "No purchases found for that item")
    })
    public ResponseEntity<Void> removeAll(
        @PathVariable Integer itemId
    ) {
        int hhId = getHhId();
        service.removeAll(hhId, itemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a specific purchase of an item in a household's inventory.
     *
     * @param hhId the ID of the household
     * @param itemId the ID of the item to delete
     * @param acquiredDate the date when the item was acquired
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @DeleteMapping("/{itemId}/{acquiredDate}")
    @Operation(summary = "Remove a specific purchase of an item")
    @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Purchase removed"),
      @ApiResponse(responseCode = "404", description = "Inventory entry not found")
    })
    public ResponseEntity<Void> removeOne(
        @PathVariable Integer itemId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime acquiredDate
    ) {
        int hhId = getHhId();
        service.remove(hhId, itemId, acquiredDate);
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates or links an item and adds it to a household's inventory in one call.
     *
     * @param hhId the ID of the household
     * @param req the request body containing item details
     * @return a {@link ResponseEntity} containing the created item
     */
    @PostMapping("/upsert")
    @Operation(summary = "Create or link an item and add to inventory in one call")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Inventory entry created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Household or Item not found")
    })
    public ResponseEntity<HouseholdItemResponse> upsert(
        @Valid @RequestBody UpsertInventoryRequest req
    ) {
        int hhId = getHhId();
        HouseholdItemResponse created = service.upsert(hhId, req);
        URI location = URI.create(String.format(
            "/api/households/%d/items/%d/%s",
            hhId,
            created.getItemId(),
            created.getAcquiredDate()
        ));
        return ResponseEntity.created(location).body(created);
    }

    private int getHhId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        return user.getHouseholdId();
    }
}
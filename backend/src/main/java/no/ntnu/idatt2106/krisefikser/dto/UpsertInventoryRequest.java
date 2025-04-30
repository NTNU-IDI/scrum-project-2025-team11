package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Composite DTO for adding an inventory entry.
 * If itemId is provided, links to existing; else creates a new Item.
 */
@Getter
@Setter
public class UpsertInventoryRequest {
    /** Existing item to link, or null to create new */
    private Integer itemId;

    /** If creating new item, its name (required when itemId is null) */
    @NotBlank(message = "Item name must be provided when itemId is null")
    private String name;

    private String description;

    // Inventory-specific fields
    @NotNull
    private BigDecimal quantity;

    private String unit;

    @NotNull
    private LocalDate acquiredDate;

    private LocalDate expirationDate;
}
package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private Integer itemId;

    // no longer always @NotBlank
    private String name;
    private String description;

    @NotNull
    private BigDecimal quantity;
    private String unit;

    @NotNull
    private LocalDateTime acquiredDate;
    private LocalDate expirationDate;

    /**
     * Validates that if itemId is not provided, we have name & description.
     */
    @AssertTrue(message = "When itemId is absent you must provide name and description")
    public boolean isNameAndDescriptionProvided() {
        if (itemId == null) {
            return name  != null && !name.isBlank()
                && description != null && !description.isBlank();
        }
        return true;
    }
}
package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating or updating a household item.
 */
@Getter
@Setter
public class HouseholdItemRequest {

    private Integer itemId;

    @NotNull
    private BigDecimal quantity;

    private String unit;
    private LocalDateTime acquiredDate;
    private LocalDate expirationDate;
}
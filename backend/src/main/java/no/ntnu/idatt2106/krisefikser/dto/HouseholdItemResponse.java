package no.ntnu.idatt2106.krisefikser.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for representing a household item in the response.
 */
@Getter
@AllArgsConstructor
public class HouseholdItemResponse {
    private Integer householdId;
    private Integer itemId;
    private BigDecimal quantity;
    private String unit;
    private LocalDate acquiredDate;
    private LocalDate expirationDate;
}

package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseholdItemRequest {

    @NotNull
    private Integer itemId;

    @NotNull
    private BigDecimal quantity;

    private String unit;
    private LocalDate acquiredDate;
    private LocalDate expirationDate;
}
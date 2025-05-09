package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO for updating a household.
 */
@Data
public class HouseholdUpdateDTO {
    @Schema(description = "Name of household you wish to change", example = "Garv sin hule")
    private String name;

    @Schema(description = "How many members exist in the household", example = "4")
    private int memberCount;
    
}

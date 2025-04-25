package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HouseholdResponseDTO {
    @Schema(description = "Unique identifier in the household table", example = "1")
    private int id;

    @Schema(description = "Name of the household", example = "Garv sin hule")
    private String name;

    @Schema(description = "How many members belong to the given household", example = "4")
    private Integer memberCount;
    
    private AddressResponseDTO address; 
}

package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseholdRequestDTO {
    @Schema(description = "The name of the household", example = "")
    private String name;
    private int memberCount;
    private AddressRequestDTO address;
}

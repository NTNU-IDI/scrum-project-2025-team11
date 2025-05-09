package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.idatt2106.krisefikser.model.Enums;

/**
 * DTO for creating or updating a point of interest.
 */
@Data
@NoArgsConstructor
public class PointOfInterestRequestDTO {
    @Schema(description = "Name of the point", example = "St. Olavs Hospital")
    private String name;

    @Schema(description = "Set of enums that lets us know what kind of point it is", example = "shelter")
    private Enums.IconEnum iconType;

    @Schema(description = "A longer description of the point containing additional information",
            example = "Største sykehuset som finnes i Trøndelag")
    private String description;

    @Schema(description = "The latitude of the location.", example = "18.5555")
    private Double latitude;

    @Schema(description = "The longitude of the location.", example = "18.5555")
    private Double longitude;
}

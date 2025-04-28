package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.idatt2106.krisefikser.model.Enums;

@Data
@NoArgsConstructor
public class PointOfInterestRequestDTO {
    private String name;
    private Enums.IconEnum iconType;
    private String description;
    private Double latitude;
    private Double longitude;

}

package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Data;
import no.ntnu.idatt2106.krisefikser.model.Enums;

@Data

public class PointOfInterestResponseDTO {

    private int id;
    private String name;
    private Enums.IconEnum iconType;
    private String description;
    private Double latitude;
    private Double longitude;
}

package no.ntnu.idatt2106.krisefikser.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "point_of_interest")
public class PointOfInterest {


    @Schema(description = "Unique identifier for the point of interest.", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Schema(description = "Name of the point", example = "St. Olavs Hospital")
    @Column(name = "name", nullable = false)
    private String name;

    @Schema(description = "Enum representing what type of point it is", example = "Hospital")
    @Enumerated(EnumType.STRING)
    @Column(name = "icon_type", nullable = false)
    private Enums.IconEnum iconType;

    @Schema(
            description = "String containing the description of the point of interest",
            example = "Biggest hospital in Trøndelag")
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * The latitude of the location.
     * This field is required and cannot be null.
     */
    @Schema(description = "The latitude of the location.", example = "73.4305")
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    /**
     * The longitude of the location.
     * This field is required and cannot be null.
     */
    @Schema(description = "The longitude of the location.", example = "18.5555")
    @Column(name = "longitude", nullable = false)
    private Double longitude;
}

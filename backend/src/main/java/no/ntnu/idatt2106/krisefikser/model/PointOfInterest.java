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

    /**
     * Unique identifier given to each point
     */
    @Schema(description = "Unique identifier for the point of interest.", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * A name given to each point so that they are easier to understand what the point is
     */
    @Schema(description = "Name of the point", example = "St. Olavs Hospital")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Category represented in enums so that we can keep track of the different
     * types of points we have.
     */
    @Schema(description = "Enum representing what type of point it is", example = "Hospital")
    @Enumerated(EnumType.STRING)
    @Column(name = "icon_type", nullable = false)
    private Enums.IconEnum iconType;

    /**
     * A more lengthy description that will give the user additional information about
     * the point.
     */
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

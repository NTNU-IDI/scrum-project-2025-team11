package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Item Response
 * This class is used to transfer item data in the response.
 * 
 * It contains the following fields:
 * - id: Unique identifier for the item
 * - name: Name of the item
 * - description: Detailed description of the item
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Data returned for an Item")
public class ItemResponse {

    @Schema(description = "Unique identifier for the item", example = "1")
    private Integer id;

    @Schema(description = "Name of the item", example = "Vann")
    private String name;

    @Schema(description = "Detailed description of the item", example = "Flasker med drikkevann, 1 L hver")
    private String description;
}

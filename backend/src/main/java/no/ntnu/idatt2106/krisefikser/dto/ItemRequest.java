package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO for creating or updating an item.
 */
@Getter @Setter @NoArgsConstructor
@Schema(description = "Payload for creating or updating an Item")
public class ItemRequest {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Name of the item", example = "Vann", required = true)
    private String name;

    @Size(max = 2000)
    @Schema(description = "Detailed description of the item", example = "Flasker med drikkevann, 1 L hver")
    private String description;
}
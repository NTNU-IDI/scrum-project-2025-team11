package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO for returning address data to the client.
 */
@Data
public class AddressResponseDTO {

  @Schema(description = "Unique identifier for the address.", example = "1")
  private int id;

  @Schema(description = "The street address of the location.", example = "Heiabakken 123")
  private String street;

  @Schema(description = "The postal code of the location.", example = "1234")
  private String postalCode;

  @Schema(description = "The city of the location.", example = "Trondheim")
  private String city;

  @Schema(description = "The latitude of the location.", example = "73.4305")
  private Double latitude;

  @Schema(description = "The longitude of the location.", example = "18.5555")
  private Double longitude;
}
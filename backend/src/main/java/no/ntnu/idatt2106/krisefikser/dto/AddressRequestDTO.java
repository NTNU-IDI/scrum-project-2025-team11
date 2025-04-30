package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO for creating or updating an address.
 */
@Data
public class AddressRequestDTO {

  @Schema(description = "The street address of the location.", example = "Heiabakken 123", required = true)
  private String street;

  @Schema(description = "The postal code of the location.", example = "1234", required = true)
  private String postalCode;

  @Schema(description = "The city of the location.", example = "Trondheim", required = true)
  private String city;

  @Schema(description = "The latitude of the location.", example = "73.4305", required = true)
  private Double latitude;

  @Schema(description = "The longitude of the location.", example = "18.5555", required = true)
  private Double longitude;
}
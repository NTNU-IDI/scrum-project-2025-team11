package no.ntnu.idatt2106.krisefikser.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating or updating an event.
 */
@Data
public class EventRequestDTO {
  
  
  @Schema(description = "Name of the event.", example = "Jordskjelv i Trondheim")
  @NotBlank(message = "Name is mandatory")
  @Size(max = 100, message = "Name must be less than 100 characters")
  private String name;

  @Schema(description = "Description of the event.", example = "Det har vært et jordskjelv i Trondheim.")
  @NotBlank(message = "Description is mandatory")
  private String description;

  @Schema(description = "Type of the event. NORMAL, SHELTER, OTHER, etc", example = "danger")
  @NotNull(message = "Icon type is mandatory")
  private String iconType;

  @Schema(description = "Start time of the event.", example = "2023-10-01T12:00:00")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @NotNull(message = "Start time is mandatory")
  private String startTime;

  @Schema(description = "End time of the event.", example = "2023-10-01T14:00:00")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private String endTime;

  @Schema(description = "Latitude of the event location.", example = "63.4305")
  @NotNull(message = "Latitude is mandatory")
  @Digits(integer = 10, fraction = 7, message = "Latitude must be a valid coordinate")
  @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
  @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
  private Double latitude;

  @Schema(description = "Longitude of the event location.", example = "10.3951")
  @NotNull(message = "Longitude is mandatory")
  @Digits(integer = 10, fraction = 7, message = "Longitude must be a valid coordinate")
  @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
  @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
  private Double longitude;

  @Schema(description = "Radius of the event location in meters.", example = "2500")
  @NotNull(message = "Radius is mandatory")
  @Digits(integer = 10, fraction = 0, message = "Radius must be a whole number")
  @Positive(message = "Radius must be positive")
  private int radius;

  @Schema(description = "Severity level, from 0 (lowest) to 5 (highest)", example = "3")
  @NotNull(message = "Severity is mandatory")
  @Digits(integer = 1, fraction = 0, message = "Severity must be a whole number between 0 and 5")
  @Min(0)
  @Max(5)
  private Integer severity;
}

package no.ntnu.idatt2106.krisefikser.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 
 */
@Data
public class EventRequestDTO {
  
  
  @Schema(description = "Name of the event.", example = "Jordskjelv i Trondheim")
  @NotBlank(message = "Name is mandatory")
  @Max(value = 100, message = "Name must be less than 100 characters")
  private String name;

  @Schema(description = "Description of the event.", example = "Det har vært et jordskjelv i Trondheim.")
  @NotBlank(message = "Description is mandatory")
  private String description;

  @Schema(description = "Type of the event. NORMAL, SHELTER, OTHER, etc", example = "MEDICAL")
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
  private Double latitude;

  @Schema(description = "Longitude of the event location.", example = "10.3951")
  @NotNull(message = "Longitude is mandatory")
  private Double longitude;

  @Schema(description = "Radius of the event location in meters.", example = "100")
  @NotNull(message = "Radius is mandatory")
  private int radius;

  @Schema(description = "Severity level, from 0 (lowest) to 5 (highest)", example = "3")
  @NotNull(message = "Severity is mandatory")
  @Min(0)
  @Max(5)
  private int severity;
}

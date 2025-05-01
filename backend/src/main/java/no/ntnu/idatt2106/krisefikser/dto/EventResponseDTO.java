package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EventResponseDTO {
  
  @Schema(description = "Unique identifier for the event.", example = "1")
  private int id;

  @Schema(description = "Name of the event.", example = "Jordskjelv i Trondheim")
  private String name;

  @Schema(description = "Description of the event.", example = "Det har vært et kraftig jordskjelv i Trondheim.")
  private String description;

  @Schema(description = "Type of the event. Like 'normal', 'shelter', 'other', etc", example = "medical")
  private String iconType;

  @Schema(description = "Start time of the event.", example = "2023-10-01T12:00:00")
  private String startTime;

  @Schema(description = "End time of the event.", example = "2023-10-01T14:00:00")
  private String endTime;

  @Schema(description = "Severity level, from 0 (lowest) to 5 (highest)", example = "3")
  private Double latitude;

  @Schema(description = "Longitude of the event location.", example = "10.3951")
  private Double longitude;

  @Schema(description = "Radius of the event location in meters.", example = "100")
  private int radius;

  @Schema(description = "Severity level, from 0 (lowest) to 5 (highest)", example = "3")
  private Integer severity;
}

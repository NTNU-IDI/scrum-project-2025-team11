package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.EventRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
import no.ntnu.idatt2106.krisefikser.exceptionhandler.ResourceNotFoundException;
import no.ntnu.idatt2106.krisefikser.service.EventService;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Event", description = "Operations related to events")
public class EventController {
  private final EventService eventService;

  /**
   * Retrieves an event by its ID.
   * @param id the ID of the event to retrieve
   * @return a {@link EventResponseDTO} containing the event details if found, or a 404 error if not found
   */
  @Operation(
    summary = "Get event by ID", 
    description = "Retrieve an event by its ID"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Event found", 
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDTO.class))), 
    @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getEventById(
    @Parameter (description = "the unique identifier of the event to retrieve", required = true, example = "1")
    @PathVariable int id) {
    return eventService.getEventById(id)
      .map(ResponseEntity::ok)
      .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
  }

  /**
   * Retrieves an event by its name.
   * @param name the name of the event to retrieve
   * @return a {@link EventResponseDTO} containing the event details if found, or a 404 error if not found
   */
  @Operation(
    summary = "Get event by name", 
    description = "Retrieve an event by its name"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Event found", 
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDTO.class))), 
    @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @GetMapping("/name/{name}")
  public ResponseEntity<EventResponseDTO> getEventByName(
    @Parameter (description = "the name of the event to retrieve", required = true, example = "Jordskjelv i Oslo")
    @PathVariable String name) {
    return eventService.getEventByName(name)
      .map(ResponseEntity::ok)
      .orElseThrow(() -> new ResourceNotFoundException("Event", "name", name));
  }

  /**
   * Retrieves all events based on the specified filters.
   * Filters can be a combination of name, icon type, start time, end time, and severity.
   * @param name the name of the event. It can be a partial match and is case-insensitive.
   * @param iconType the icon type of the event. Must match one of the predefined types exactly.
   * @param startTime the time you want to filter to get events that start after this time.
   * @param endTime the time you want to filter to get events that end before this time.
   * @param severity the severity of the event. Must match one of the predefined types exactly.
   * @return a list of {@link EventResponseDTO} containing the filtered events, ifno events are found, returns a 204 No Content response.
   */
  @Operation(
    summary = "Get all events", 
    description = "Retrieve all events based on the specified filters"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Events found", 
                  content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class)))), 
    @ApiResponse(responseCode = "204", description = "No events found")
  })
  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getAllEvents(
    @Parameter (description = "the name of the event to filter by", example = "Jordskjelv i Oslo")
    @RequestParam(required = false) String name,
    @Parameter (description = "the icon type of the event to filter by", example = "danger")
    @RequestParam(required = false) String iconType,
    @Parameter (description = "the start time of the event to filter by. Events with start time after the given time will be returned", example = "2025-02-19T12:50:00")
    @RequestParam(required = false) String startTime,
    @Parameter (description = "the end time of the event to filter by. Events with end time before the given time will be returned", example = "2025-10-01T12:00:00")
    @RequestParam(required = false) String endTime,
    @Parameter (description = "the severity of the event to filter by", example = "3")
    @RequestParam(required = false) Integer severity) {
    List<EventResponseDTO> events = eventService.getFilteredEvents(name, iconType, startTime, endTime, severity);
    if (!events.isEmpty()) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  /**
   * Retrieve all active events.
   * An event is considered active if its start time is in the past and its end time is in the future.
   * @return a list of {@link EventResponseDTO} containing all active events, or a 204 No Content response if no active events are found.
   */
  @Operation(
    summary = "Get all active events", 
    description = "Retrieve all active events"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Active events found", 
                  content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class)))), 
    @ApiResponse(responseCode = "204", description = "No active events found")
  })
  @GetMapping("/active")
  public ResponseEntity<List<EventResponseDTO>> getActiveEvents() {
    List<EventResponseDTO> events = eventService.getActiveEvents();
    if (!events.isEmpty()) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  /**
   * Retrieves all upcoming events.
   * @return a list of {@link EventResponseDTO} containing all upcoming events, or a 204 No Content response if no upcoming events are found.
   */
  @Operation(
    summary = "Get all upcoming events", 
    description = "Retrieve all upcoming events"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Upcoming events found", 
                  content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class)))), 
    @ApiResponse(responseCode = "204", description = "No upcoming events found")
  })
  @GetMapping("/upcoming")
  public ResponseEntity<List<EventResponseDTO>> getUpcomingEvents() {
    List<EventResponseDTO> events = eventService.getUpcomingEvents();
    if (!events.isEmpty()) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  /**
   * Retrieves all past events.
   * @return a list of {@link EventResponseDTO} containing all past events, or a 204 No Content response if no past events are found.
   */
  @Operation(
    summary = "Get all past events", 
    description = "Retrieve all past events"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Past events found", 
                  content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class)))), 
    @ApiResponse(responseCode = "204", description = "No past events found")
  })
  @GetMapping("/past")
  public ResponseEntity<List<EventResponseDTO>> getPastEvents() {
    List<EventResponseDTO> events = eventService.getPastEvents();
    if (!events.isEmpty()) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  /**
   * Gets events by icon type.
   * @param iconType the icon type of the event to filter by
   * @return a list of {@link EventResponseDTO} containing the events with the specified icon type, or a 204 No Content response if no events are found.
   */
  @Operation(
    summary = "Get events by icon type", 
    description = "Retrieve events by icon type"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Events found", 
                  content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class)))), 
    @ApiResponse(responseCode = "204", description = "No events found")
  })
  @GetMapping("/icon/{iconType}")
  public ResponseEntity<List<EventResponseDTO>> getEventsByIconType(
    @Parameter (description = "the icon type of the event to filter by", required = true, example = "danger")
    @PathVariable String iconType) {
    List<EventResponseDTO> events = eventService.getEventsByIconType(iconType);
    if (!events.isEmpty()) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  /**
   * Retrieves all events affecting a given location. 
   * The location is defined by latitude and longitude
   * The events that will return all events whose circle of influence (center + radius)
   * contains the given (latitude, longitude) point.
   * @param lat the latitude of the location
   * @param lon the longitude of the location
   * @return a list of {@link EventResponseDTO} containing the events affecting the specified location, or a 204 No Content response if no events are found.
   */
  @Operation(
    summary = "Get events affecting location",
    description = "Retrieve events affecting a specified location"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Events found", 
                  content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class)))), 
    @ApiResponse(responseCode = "204", description = "No events found")
  })
  @GetMapping("/affecting")
  public ResponseEntity<List<EventResponseDTO>> getEventsAffecting(
    @Parameter (description = "the latitude of the location", required = true, example = "60.39299")
    @RequestParam double lat,
    @Parameter (description = "the longitude of the location", required = true, example = "5.32415")
    @RequestParam double lon) {
    List<EventResponseDTO> events = eventService.getEventsAffectingLocation(lat, lon);
    if (!events.isEmpty()) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  /**
   * Saves a new event to the database.
   * @param event the event to save as a {@link EventResponseDTO} object
   * @return a {code ResponseEntity} containing the saved event as a {@link EventResponseDTO} object, or a 400 Bad Request response if the event is invalid.
   */
  @Operation(
    summary = "Save a new event", 
    description = "Save a new event to the database"
    //,security = @SecurityRequirement(name = "bearer-key")
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Event saved successfully", 
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDTO.class))), 
    @ApiResponse(responseCode = "400", description = "Invalid event data")
  })
  @PostMapping
  public ResponseEntity<EventResponseDTO> saveEvent(
    @Parameter (description = "the event to save", required = true)
    @Valid @RequestBody EventRequestDTO event) {
    EventResponseDTO savedEvent = eventService.saveEvent(event);
    return ResponseEntity.ok(savedEvent);
  }

  /**
   * Updates an existing event in the database.
   * @param id the ID of the event to update
   * @param event the updated event data as a {@link EventRequestDTO} object
   * @return a {@link EventResponseDTO} containing the updated event details, or a 404 Not Found response if the event is not found.
   */
  @Operation(
    summary = "Update an existing event", 
    description = "Update an existing event in the database"
    //,security = @SecurityRequirement
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Event updated successfully", 
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDTO.class))), 
    @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @PutMapping("/{id}")
  public ResponseEntity<EventResponseDTO> updateEvent(
    @Parameter (description = "the ID of the event to update", required = true, example = "1")
    @PathVariable int id,
    @Parameter (description = "the updated event data", required = true)
    @Valid @RequestBody EventRequestDTO event) {
    return ResponseEntity.ok(eventService.updateEvent(id, event));
  }
  
  /**
   * Deletes an event from the database by its ID.
   * @param id the ID of the event to delete
   * @return a 204 No Content response if the event is deleted successfully, or a 404 Not Found response if the event is not found.
   */
  @Operation(
    summary = "Delete an event", 
    description = "Delete an event from the database by its ID"
    //,security = @SecurityRequirement
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Event deleted successfully"), 
    @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEvent(
    @Parameter (description = "the ID of the event to delete", required = true, example = "1")
    @PathVariable int id) {
    eventService.deleteEvent(id);
    return ResponseEntity.noContent().build();
  }
}

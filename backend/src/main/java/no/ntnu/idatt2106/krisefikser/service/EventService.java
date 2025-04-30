package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.EventRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.EventMapper;
import no.ntnu.idatt2106.krisefikser.model.Event;
import no.ntnu.idatt2106.krisefikser.repository.EventRepository;


@Service
@RequiredArgsConstructor
public class EventService {
  
  private final EventRepository eventRepository;
  private final EventMapper mapper;
  private static final double EARTH_RADIUS_M = 6_371_000;

  /**
   * Retrieves an event by its ID.
   * @param id the ID of the event to retrieve
   * @return an Optional containing the EventResponseDTO if found, or an empty Optional if not found
   */
  public Optional<EventResponseDTO> getEventById(int id) {
    return eventRepository.findById(id).map(mapper::toResponseDTO);
  }

  /**
   * Retrieves an event by its name.
   * @param name the name of the event to retrieve. It can be a partial match, and is case-insensitive.   * 
   * @return an Optional containing the EventResponseDTO if found, or an empty Optional if not found
   */
  public Optional<EventResponseDTO> getEventByName(String name) {
    return Optional.ofNullable(eventRepository.findByName(name)).map(mapper::toResponseDTO);
  }

  /**
   * Retrieves a list of events filtered by various criteria.
   * @param name the name of the event to filter by (optional)
   * @param iconType the icon type of the event to filter by (optional)
   * @param startTime the time you want to filter to get all events starting after this time (optional)
   * @param endTime the time you want to filter to get all events ending before this time (optional)
   * @param severity the severity level of the event to filter by (0-5, optional)
   * @return a list of EventResponseDTOs matching the specified criteria
   */
  public List<EventResponseDTO> getFilteredEvents(String name, String iconType, String startTime, String endTime, int severity) throws IllegalArgumentException{
    Specification<Event> spec = Specification.where(null);

    if (name != null) {
      spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }
    if (iconType != null) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("iconType"), iconType));
    }
    if (startTime != null) {
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startTime"), startTime));
    }
    if (endTime != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("endTime"), endTime));
    }
    if (severity >= 0 && severity <= 5) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("severity"), severity));
    } else if (severity < 0) {
      throw new IllegalArgumentException("Severity must be between 0 and 5");
    }

    return eventRepository.findAll(spec)
        .stream()
        .map(mapper::toResponseDTO)
        .toList();
  }

  public List<EventResponseDTO> getActiveEvents() {
    return eventRepository.findAll((root, query, cb) -> 
      cb.and(
      cb.lessThanOrEqualTo(root.get("startTime"), cb.currentTimestamp()),
      cb.greaterThanOrEqualTo(root.get("endTime"), cb.currentTimestamp()))
      )
      .stream()
      .map(mapper::toResponseDTO)
      .toList();
  }

  public List<EventResponseDTO> getUpcomingEvents() {
    return eventRepository.findAll((root, query, cb) -> 
      cb.lessThanOrEqualTo(root.get("startTime"), cb.currentTimestamp()))
      .stream()
      .map(mapper::toResponseDTO)
      .toList();
  }

  public List<EventResponseDTO> getPastEvents() {
    return eventRepository.findAll((root, query, cb) -> 
      cb.lessThanOrEqualTo(root.get("endTime"), cb.currentTimestamp()))
      .stream()
      .map(mapper::toResponseDTO)
      .toList();
  }

  public List<EventResponseDTO> getEventsByIconType(String iconType) {
    return eventRepository.findByIconTypeOrderByStartTimeDesc(iconType)
        .stream()
        .toList();
  }

 /**
   * Returns all events whose circle of influence (center + radius)
   * contains the given (lat, lon) point.
   */
  public List<EventResponseDTO> getEventsNear(double lat, double lon) {
    // size a bounding-box around (lat, lon)
    int maxRadiusM = eventRepository.findMaxRadius();        // e.g. 500 meters, or larger
    double radiusKm = maxRadiusM / 1_000.0;       // convert to km
    double degLat = radiusKm / 110.574;          // ~km per degree latitude
    double degLon = radiusKm / (111.320 * Math.cos(Math.toRadians(lat)));   // ~km per degree longitude

    // fetch only events whose centers lie in that box
    List<Event> candidates = eventRepository.findAllInBox(lat, lon, degLat, degLon);

    // exact Haversine check per event 
    return candidates.stream()
      .filter(e -> haversine(lat, lon, e.getLatitude(), e.getLongitude())
                    <= e.getRadius())
      .map(mapper::toResponseDTO)
      .collect(Collectors.toList());
  }

  // Haversine distance in meters
  private static double haversine(double lat1, double lon1, double lat2, double lon2) {
    double φ1 = Math.toRadians(lat1);
    double φ2 = Math.toRadians(lat2);
    double Δφ = Math.toRadians(lat2 - lat1);
    double Δλ = Math.toRadians(lon2 - lon1);

    double a = Math.sin(Δφ/2) * Math.sin(Δφ/2)
             + Math.cos(φ1) * Math.cos(φ2)
             * Math.sin(Δλ/2) * Math.sin(Δλ/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS_M * c;
  }

  /**
   * Saves a new event to the database.
   * @param event the Event request as a {@link EventRequestDTO} object
   * @return the saved Event as a response DTO {@link EventResponseDTO}
   */
  public EventResponseDTO saveEvent(EventRequestDTO event) {
    Event entity = mapper.toEntity(event);
    Event savedEvent = eventRepository.save(entity);
    return mapper.toResponseDTO(savedEvent);
  }

  /**
   * Updates an existing event in the database.
   * @param id the ID of the event to update
   * @param event the updated Event request as a {@link EventRequestDTO} object
   * @return the updated Event as a response DTO {@link EventResponseDTO}
   */
  public EventResponseDTO updateEvent(int id, EventRequestDTO event) {
    Event entity = mapper.toEntity(event);
    Event updatedEvent = eventRepository.save(entity);
    return mapper.toResponseDTO(updatedEvent);
  }

  /**
   * Deletes an event from the database by its ID.
   * @param id the ID of the event to delete
   */
  public void deleteEvent(int id) {
    eventRepository.deleteById(id);
  }
}

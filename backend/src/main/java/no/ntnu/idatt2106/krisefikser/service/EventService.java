package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Expression;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.EventMapper;
import no.ntnu.idatt2106.krisefikser.model.Event;
import no.ntnu.idatt2106.krisefikser.repository.EventRepository;


@Service
@RequiredArgsConstructor
public class EventService {
  
  private final EventRepository eventRepository;
  private final EventMapper mapper;

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
   * @param name the name of the event to retrieve
   * @return an Optional containing the EventResponseDTO if found, or an empty Optional if not found
   */
  public Optional<EventResponseDTO> getEventByName(String name) {
    return Optional.ofNullable(eventRepository.findByName(name)).map(mapper::toResponseDTO);
  }

  public List<EventResponseDTO> getfilteredEvents(String name, String iconType, String startTime, String endTime, int severity) {
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
      cb.greaterThanOrEqualTo(root.get("endTime"), cb.currentTimestamp()))
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
   * Retrieves a list of events near a specified location within a given radius.
   * @param lat the latitude of the location
   * @param lon the longitude of the location
   * @param radius the radius in meters within which to search for events
   * @return a list of EventResponseDTOs representing the events found within the specified radius
   */
    public List<EventResponseDTO> getEventsNear(double lat, double lon, int radius) {
      List<Event> events;
      if (radius <= 20000) { // Short distances up to 20 km
          events = getEventsNearShortDistance(lat, lon, radius);
      } else { // Longer distances
          events = getEventsNearLongDistance(lat, lon, radius);
      }
      // Map the raw Event entities to EventResponseDTOs
      return events.stream()
                   .map(mapper::toResponseDTO)
                   .toList();
  }
  
  private List<Event> getEventsNearShortDistance(double lat, double lon, int radius) {
      return eventRepository.findAll((root, query, cb) -> {
          return cb.and(
              cb.lessThanOrEqualTo(root.get("latitude"), lat + radius / 111320.0),
              cb.greaterThanOrEqualTo(root.get("latitude"), lat - radius / 111320.0),
              cb.lessThanOrEqualTo(root.get("longitude"), lon + radius / (111320.0 * Math.cos(Math.toRadians(lat)))),
              cb.greaterThanOrEqualTo(root.get("longitude"), lon - radius / (111320.0 * Math.cos(Math.toRadians(lat))))
          );
      });
  }
  
    private List<Event> getEventsNearLongDistance(double lat, double lon, int radius) {
      double earthRadius = 6371000; // Earth's radius in meters
  
      // Convert radius from meters to radians
      double radiusInRadians = radius / earthRadius;
  
      // Use the Haversine formula to filter events
      return eventRepository.findAll((root, query, cb) -> {
          // Expressions for Haversine formula components
          Expression<Double> cosLat1 = cb.function("cos", Double.class, cb.function("radians", Double.class, cb.literal(lat)));
          Expression<Double> cosLat2 = cb.function("cos", Double.class, cb.function("radians", Double.class, root.get("latitude")));
          Expression<Double> sinLat1 = cb.function("sin", Double.class, cb.function("radians", Double.class, cb.literal(lat)));
          Expression<Double> sinLat2 = cb.function("sin", Double.class, cb.function("radians", Double.class, root.get("latitude")));
          Expression<Double> cosLonDiff = cb.function("cos", Double.class, cb.function("radians", Double.class, cb.diff(root.get("longitude"), lon)));
  
          // Break the summation into steps
          Expression<Double> firstPart = cb.prod(cosLat1, cosLat2);
          Expression<Double> secondPart = cb.prod(sinLat1, sinLat2);
          Expression<Double> totalSum = cb.sum(firstPart, cb.prod(cosLat2, cosLonDiff));
  
          // Final Haversine formula
          return cb.and(
              cb.lessThanOrEqualTo(
                  cb.function("acos", Double.class, cb.sum(totalSum, secondPart)),
                  radiusInRadians
              )
          );
      });
  }
}

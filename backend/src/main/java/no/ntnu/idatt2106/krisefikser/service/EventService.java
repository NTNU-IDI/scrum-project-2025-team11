package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
}

package no.ntnu.idatt2106.krisefikser.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import no.ntnu.idatt2106.krisefikser.dto.EventRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Event;

/**
 * This mapper class is responsible for mapping data between the Event entity and its corresponding DTOs.
 */
public class EventMapper {
  public EventResponseDTO toResponseDTO(Event entity) {
    EventResponseDTO dto = new EventResponseDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setIconType(entity.getIconType().toString()); 
    dto.setStartTime(entity.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    if (entity.getEndTime() != null) {
      dto.setEndTime(entity.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    } else {
      dto.setEndTime(null);
    }
    dto.setLatitude(entity.getLatitude());
    dto.setLongitude(entity.getLongitude());
    dto.setRadius(entity.getRadius());
    dto.setSeverity(entity.getSeverity());
    return dto;
  }

  public Event toEntity(EventRequestDTO dto) {
    Event event = new Event();
    event.setName(dto.getName());
    event.setDescription(dto.getDescription());
    event.setIconType(Event.IconType.valueOf(dto.getIconType()));
    event.setStartTime(LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    if (dto.getEndTime() != null) {
      event.setEndTime(LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
    event.setLatitude(dto.getLatitude());
    event.setLongitude(dto.getLongitude());
    event.setRadius(dto.getRadius());
    event.setSeverity(dto.getSeverity());
    return event;
}
}

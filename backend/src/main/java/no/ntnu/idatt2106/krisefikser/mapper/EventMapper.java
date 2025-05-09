package no.ntnu.idatt2106.krisefikser.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import no.ntnu.idatt2106.krisefikser.dto.EventRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Enums;
import no.ntnu.idatt2106.krisefikser.model.Event;

/**
 * Component responsible for converting between Event entities and their DTO representations.
 * <p>
 * Provides methods to map Event to EventResponseDTO for API output,
 * and to map EventRequestDTO to Event entity for persistence.
 */
@Component
public class EventMapper {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Maps an Event entity to a response DTO.
     *
     * @param entity the Event entity to convert
     * @return a populated EventResponseDTO with formatted date strings and all fields
     */
    public EventResponseDTO toResponseDTO(Event entity) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setIconType(entity.getIconType().toString());
        dto.setStartTime(entity.getStartTime().format(FORMATTER));
        dto.setEndTime(
            entity.getEndTime() != null
                ? entity.getEndTime().format(FORMATTER)
                : null
        );
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setRadius(entity.getRadius());
        dto.setSeverity(entity.getSeverity());
        return dto;
    }

    /**
     * Maps an EventRequestDTO to a new Event entity.
     *
     * @param dto the incoming DTO with raw data and formatted date strings
     * @return a new Event entity populated with values parsed from the DTO
     */
    public Event toEntity(EventRequestDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setIconType(Enums.IconEnum.valueOf(dto.getIconType()));
        event.setStartTime(LocalDateTime.parse(dto.getStartTime(), FORMATTER));
        if (dto.getEndTime() != null) {
            event.setEndTime(LocalDateTime.parse(dto.getEndTime(), FORMATTER));
        }
        event.setLatitude(dto.getLatitude());
        event.setLongitude(dto.getLongitude());
        event.setRadius(dto.getRadius());
        event.setSeverity(dto.getSeverity());
        return event;
    }
}

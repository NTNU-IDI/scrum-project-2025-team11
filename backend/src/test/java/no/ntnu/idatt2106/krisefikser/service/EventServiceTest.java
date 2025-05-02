package no.ntnu.idatt2106.krisefikser.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import no.ntnu.idatt2106.krisefikser.dto.EventRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.EventMapper;
import no.ntnu.idatt2106.krisefikser.model.Event;
import no.ntnu.idatt2106.krisefikser.repository.EventRepository;
import no.ntnu.idatt2106.krisefikser.model.Enums.IconEnum;

public class EventServiceTest {
  @Mock
  private EventRepository eventRepository;

  @Mock
  private EventMapper eventMapper;

  @InjectMocks
  private EventService eventService;

  private Event event;
  private EventResponseDTO eventResponseDTO;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  @BeforeEach
  void setup() {
    event = new Event();
    event.setId(1);
    event.setName("Test Event");
    event.setDescription("Test Description");
    event.setStartTime(LocalDateTime.parse("2023-10-01T10:00:00"));
    event.setEndTime(LocalDateTime.parse("2023-10-01T12:00:00"));
    event.setIconType(IconEnum.medical);
    event.setLatitude(45.44919);
    event.setLongitude(-160.18513);
    event.setRadius(1000);
    event.setSeverity(2);

    eventResponseDTO = new EventResponseDTO();
    eventResponseDTO.setId(event.getId());
    eventResponseDTO.setName(event.getName());
    eventResponseDTO.setDescription(event.getDescription());
    eventResponseDTO.setStartTime(event.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    eventResponseDTO.setEndTime(event.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    eventResponseDTO.setIconType(event.getIconType().toString());
    eventResponseDTO.setLatitude(event.getLatitude());
    eventResponseDTO.setLongitude(event.getLongitude());
    eventResponseDTO.setRadius(event.getRadius());
    eventResponseDTO.setSeverity(event.getSeverity());
  }

  @Nested
  @DisplayName("Positive Test cases")
  class PositiveTests {

    @Test
    void testFindById() {
      when(eventRepository.findById(1)).thenReturn(Optional.of(event));
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

      Optional<EventResponseDTO> foundEvent = eventService.getEventById(1);
      
      verify(eventRepository, times(1)).findById(1);
      verify(eventMapper, times(1)).toResponseDTO(event);
      assertNotNull(foundEvent);
      assertEquals(event.getId(), foundEvent.get().getId());
      assertEquals(event.getName(), foundEvent.get().getName());
      assertEquals(event.getRadius(), foundEvent.get().getRadius());
    }
    
    @Test
    void testCreateEvent() {
      EventRequestDTO eventRequestDTO = new EventRequestDTO();
      eventRequestDTO.setName("New Event");
      eventRequestDTO.setDescription("New Description");
      eventRequestDTO.setStartTime("2023-10-01T10:00:00");
      eventRequestDTO.setEndTime("2023-10-01T12:00:00");
      eventRequestDTO.setIconType(IconEnum.medical.toString());
      eventRequestDTO.setLatitude(45.44919);
      eventRequestDTO.setLongitude(-160.18513);
      eventRequestDTO.setRadius(1000);
      eventRequestDTO.setSeverity(2);

      when(eventMapper.toEntity(eventRequestDTO)).thenReturn(event);
      when(eventRepository.save(any(Event.class))).thenReturn(event);
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);
      
      EventResponseDTO createdEvent = eventService.saveEvent(eventRequestDTO);
      
      verify(eventRepository, times(1)).save(event);
      
      assertNotNull(createdEvent);
      assertEquals(event.getId(), createdEvent.getId());
    }
    
    @Test
    void testUpdateEvent() {
      EventRequestDTO eventRequestDTO = new EventRequestDTO();
      eventRequestDTO.setName("Updated Event");
      eventRequestDTO.setDescription("Updated Description");
      eventRequestDTO.setStartTime("2023-10-01T10:00:00");
      eventRequestDTO.setEndTime("2023-10-01T12:00:00");
      eventRequestDTO.setIconType(IconEnum.medical.toString());
      eventRequestDTO.setLatitude(45.44919);
      eventRequestDTO.setLongitude(-160.18513);
      eventRequestDTO.setRadius(9999);
      eventRequestDTO.setSeverity(4);
      
      when(eventRepository.findById(1)).thenReturn(Optional.of(event));
      when(eventMapper.toEntity(eventRequestDTO)).thenReturn(event);
      when(eventRepository.save(any(Event.class))).thenReturn(event);
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

      EventResponseDTO updatedEvent = eventService.updateEvent(1, eventRequestDTO);
      
      verify(eventRepository, times(1)).save(event);
      
      assertNotNull(updatedEvent);
      assertEquals(event.getName(), updatedEvent.getName());
      assertEquals(event.getRadius(), updatedEvent.getRadius());
      assertEquals(event.getSeverity(), updatedEvent.getSeverity());
    }
    
    @Test
    void testDeleteEvent() {
      doNothing().when(eventRepository).deleteById(1);
      
      eventService.deleteEvent(1);
      
      verify(eventRepository, times(1)).deleteById(1);
      
      assertEquals(Optional.empty(), eventRepository.findById(1));
    }
    
    @Test
    void testGetEventByName() {
      when(eventRepository.findByName("Test Event")).thenReturn(event);
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

      Optional<EventResponseDTO> foundEvent = eventService.getEventByName("Test Event");

      verify(eventRepository, times(1)).findByName("Test Event");
      assertNotNull(foundEvent);
      assertEquals(event.getId(), foundEvent.get().getId());
      assertEquals(event.getName(), foundEvent.get().getName());
    }
    
    @Test
    void testGetEventsAffectingLocation() {
      // Original event coordinates (45.44919, -160.18513) with radius 1000m
      double originalLat = event.getLatitude();
      double originalLon = event.getLongitude();
  
      // Create a test point 500m northeast of the event center (within radius)
      double testLat = originalLat + 0.0045; // ~500m north
      double testLon = originalLon + 0.0063; // ~500m east
  
      when(eventRepository.findMaxRadius()).thenReturn(event.getRadius());
      when(eventRepository.findAllInBox(
          eq(testLat), eq(testLon), any(Double.class), any(Double.class))).thenReturn(List.of(event));
  
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);
  
      List<EventResponseDTO> results = eventService.getEventsAffectingLocation(testLat, testLon);
  
      assertEquals(1, results.size(), "Should find 1 event affecting the location");
      assertEquals(event.getId(), results.get(0).getId());
  
      verify(eventRepository).findAllInBox(
          eq(testLat), eq(testLon), any(Double.class), any(Double.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetAllEvents() {
      Event event2 = new Event();
      event2.setId(2);
      event2.setName("Test Event 2");
      event2.setDescription("Test Description 2");
      event2.setStartTime(LocalDateTime.parse("2024-10-01T10:00:00"));
      event2.setEndTime(LocalDateTime.parse("2024-10-01T12:00:00"));
      event2.setIconType(IconEnum.danger);
      event2.setLatitude(85.44919);
      event2.setLongitude(100.113);
      event2.setRadius(200);
      event2.setSeverity(1);

      EventResponseDTO eventResponseDTO2 = new EventResponseDTO();
      eventResponseDTO2.setId(event2.getId());
      eventResponseDTO2.setName(event2.getName());
      eventResponseDTO2.setDescription(event2.getDescription());
      eventResponseDTO2
          .setStartTime(event2.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      eventResponseDTO2.setEndTime(event2.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      eventResponseDTO2.setIconType(event2.getIconType().toString());
      eventResponseDTO2.setLatitude(event2.getLatitude());
      eventResponseDTO2.setLongitude(event2.getLongitude());
      eventResponseDTO2.setRadius(event2.getRadius());
      eventResponseDTO2.setSeverity(event2.getSeverity());

      when(eventRepository.findAll(any(Specification.class))).thenReturn(List.of(event, event2));
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);
      when(eventMapper.toResponseDTO(event2)).thenReturn(eventResponseDTO2);

      List<EventResponseDTO> events = eventService.getFilteredEvents(null, null, null, null, null);

      assertEquals(2, events.size());
      assertEquals(event.getId(), events.get(0).getId());
      assertEquals(event2.getId(), events.get(1).getId());
    }

    @SuppressWarnings("unchecked")
    @Test
    void getFilteredEvents() {
      when(eventRepository.findAll(any(Specification.class))).thenReturn(List.of(event));
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

      List<EventResponseDTO> filteredEvents = eventService.getFilteredEvents("Test Event", null, null, null, 2);

      assertEquals(1, filteredEvents.size());
      assertEquals(event.getId(), filteredEvents.get(0).getId());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetActiveEvents() {
      Event activeEvent = new Event();
      activeEvent.setId(2);
      activeEvent.setName("Active Event");
      activeEvent.setDescription("Active Description");
      activeEvent.setStartTime(LocalDateTime.parse("2024-10-01T10:00:00"));
      activeEvent.setEndTime(LocalDateTime.parse("2025-10-01T12:00:00"));
      activeEvent.setIconType(IconEnum.none);
      activeEvent.setLatitude(85.44919);
      activeEvent.setLongitude(100.113);
      activeEvent.setRadius(200);
      activeEvent.setSeverity(1);

      EventResponseDTO activeEventDTO = new EventResponseDTO();
      activeEventDTO.setId(activeEvent.getId());
      activeEventDTO.setName(activeEvent.getName());
      activeEventDTO.setDescription(activeEvent.getDescription());
      activeEventDTO
          .setStartTime(activeEvent.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      activeEventDTO.setEndTime(activeEvent.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      activeEventDTO.setIconType(activeEvent.getIconType().toString());
      activeEventDTO.setLatitude(activeEvent.getLatitude());
      activeEventDTO.setLongitude(activeEvent.getLongitude());
      activeEventDTO.setRadius(activeEvent.getRadius());
      activeEventDTO.setSeverity(activeEvent.getSeverity());

      when(eventRepository.findAll(any(Specification.class))).thenReturn(List.of(activeEvent));
      when(eventMapper.toResponseDTO(activeEvent)).thenReturn(activeEventDTO);
      when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

      List<EventResponseDTO> activeEvents = eventService.getActiveEvents();

      assertNotNull(activeEvents);
      assertEquals(1, activeEvents.size());
      assertEquals(activeEvent.getId(), activeEvents.get(0).getId());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetUpcomingEvents() {
      Event upcomingEvent = new Event();
      upcomingEvent.setId(3);
      upcomingEvent.setName("Upcoming Event");
      upcomingEvent.setDescription("Upcoming Description");
      upcomingEvent.setStartTime(LocalDateTime.parse("2025-10-01T10:00:00"));
      upcomingEvent.setEndTime(LocalDateTime.parse("2026-10-01T12:00:00"));
      upcomingEvent.setIconType(IconEnum.none);
      upcomingEvent.setLatitude(85.44919);
      upcomingEvent.setLongitude(100.113);
      upcomingEvent.setRadius(200);
      upcomingEvent.setSeverity(1);

      EventResponseDTO upcomingEventDTO = new EventResponseDTO();
      upcomingEventDTO.setId(upcomingEvent.getId());
      upcomingEventDTO.setName(upcomingEvent.getName());
      upcomingEventDTO.setDescription(upcomingEvent.getDescription());
      upcomingEventDTO
          .setStartTime(upcomingEvent.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      upcomingEventDTO
          .setEndTime(upcomingEvent.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      upcomingEventDTO.setIconType(upcomingEvent.getIconType().toString());
      upcomingEventDTO.setLatitude(upcomingEvent.getLatitude());
      upcomingEventDTO.setLongitude(upcomingEvent.getLongitude());
      upcomingEventDTO.setRadius(upcomingEvent.getRadius());
      upcomingEventDTO.setSeverity(upcomingEvent.getSeverity());

      when(eventRepository.findAll(any(Specification.class))).thenReturn(List.of(upcomingEvent));
      when(eventMapper.toResponseDTO(upcomingEvent)).thenReturn(upcomingEventDTO);

      List<EventResponseDTO> events = eventService.getUpcomingEvents();

      assertNotNull(events);
      assertEquals(1, events.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetPastEvents() {
      Event pastEvent = new Event();
      pastEvent.setId(4);
      pastEvent.setName("Past Event");
      pastEvent.setDescription("Past Description");
      pastEvent.setStartTime(LocalDateTime.parse("2022-10-01T10:00:00"));
      pastEvent.setEndTime(LocalDateTime.parse("2023-10-01T12:00:00"));

      EventResponseDTO pastEventDTO = new EventResponseDTO();
      pastEventDTO.setId(pastEvent.getId());
      pastEventDTO.setName(pastEvent.getName());
      pastEventDTO.setDescription(pastEvent.getDescription());
      pastEventDTO.setStartTime(pastEvent.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      pastEventDTO.setEndTime(pastEvent.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

      when(eventRepository.findAll(any(Specification.class))).thenReturn(List.of(pastEvent));
      when(eventMapper.toResponseDTO(pastEvent)).thenReturn(pastEventDTO);

      List<EventResponseDTO> events = eventService.getPastEvents();

      assertNotNull(events);
      assertEquals(1, events.size());
    }

    @Test
    void testGetEventsByIconType() {
      Event event1 = new Event();
      event1.setId(1);
      event1.setIconType(IconEnum.danger);
      event1.setStartTime(LocalDateTime.now().minusDays(1));
      event1.setEndTime(LocalDateTime.now().plusDays(1));

      Event event2 = new Event();
      event2.setId(2);
      event2.setIconType(IconEnum.danger);
      event2.setStartTime(LocalDateTime.now().minusHours(3));
      event2.setEndTime(LocalDateTime.now().plusHours(3));

      EventResponseDTO dto1 = new EventResponseDTO();
      dto1.setId(1);
      dto1.setIconType("danger");

      EventResponseDTO dto2 = new EventResponseDTO();
      dto2.setId(2);
      dto2.setIconType("danger");

      when(eventRepository.findByIconTypeOrderByStartTimeDesc("danger"))
          .thenReturn(List.of(dto1, dto2));

      when(eventMapper.toResponseDTO(event1)).thenReturn(dto1);
      when(eventMapper.toResponseDTO(event2)).thenReturn(dto2);

      List<EventResponseDTO> results = eventService.getEventsByIconType("danger");

      assertEquals(2, results.size());
      assertEquals("danger", results.get(0).getIconType());
      assertEquals("danger", results.get(1).getIconType());
      verify(eventRepository, times(1)).findByIconTypeOrderByStartTimeDesc("danger");
    }
  }

  @Nested
  @DisplayName("Negative Test cases")
  class NegativeTests {

    @Test
    void shouldThrowExceptionWhenIdIsInvalid() {
      int invalidId = -1; // Example of an invalid ID
      when(eventRepository.findById(invalidId)).thenThrow(new IllegalArgumentException("ID cannot be null"));
      assertThrows(IllegalArgumentException.class, () -> eventService.getEventById(invalidId));
    }

    @Test
    void testFindByIdNotFound() {
      when(eventRepository.findById(999)).thenReturn(Optional.empty());

      Optional<EventResponseDTO> foundEvent = eventService.getEventById(999);

      verify(eventRepository, times(1)).findById(999);
      assertFalse(foundEvent.isPresent(), "Event should not be found");
    }

    @Test
    void testCreateEventWithNullName() {
      EventRequestDTO eventRequestDTO = new EventRequestDTO();
      eventRequestDTO.setName(null); // Name is null

      assertThrows(IllegalArgumentException.class, () -> eventService.saveEvent(eventRequestDTO));
    }

    @Test
    void testUpdateEventNotFound() {
      EventRequestDTO eventRequestDTO = new EventRequestDTO();
      eventRequestDTO.setName("Updated Event");

      when(eventRepository.findById(999)).thenReturn(Optional.empty());

      assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(999, eventRequestDTO));
    }
  }
}



package no.ntnu.idatt2106.krisefikser.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.EventRequestDTO;
import no.ntnu.idatt2106.krisefikser.model.Enums.IconEnum;
import no.ntnu.idatt2106.krisefikser.repository.EventRepository;
import no.ntnu.idatt2106.krisefikser.service.EventService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
public class EventControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private EventService eventService;

  @MockitoBean 
  private EventRepository eventRepository;

  @Autowired
  private ObjectMapper mapper;

  private static final String BASE_URL = "/api/events";

  @Nested
  @DisplayName("Positive test cases")
  class PositiveTests {
    @Test
    @DisplayName("GET /api/events returns list of events")
    void getAllEvents_ReturnsListOfEvents() throws Exception {
      LocalDateTime startTime = LocalDateTime.of(2024, 2, 20, 12, 0);
      LocalDateTime endTime = LocalDateTime.of(2024, 2, 20, 14, 0);

      EventResponseDTO responseDTO = new EventResponseDTO();
      responseDTO.setId(1);
      responseDTO.setName("Earthquake in Trondheim");
      responseDTO.setDescription("A strong earthquake has hit Trondheim.");
      responseDTO.setIconType(IconEnum.danger.toString());
      responseDTO.setLatitude(63.4305);
      responseDTO.setLongitude(10.3951);
      responseDTO.setStartTime(startTime.toString());
      responseDTO.setEndTime(endTime.toString());
      responseDTO.setRadius(10000);
      responseDTO.setSeverity(4);

      when(eventService.getFilteredEvents(null, null, null, null, null)).thenReturn(List.of(responseDTO));

      mvc.perform(get(BASE_URL))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(responseDTO.getId()))
          .andExpect(jsonPath("$[0].name").value(responseDTO.getName()))
          .andExpect(jsonPath("$[0].description").value(responseDTO.getDescription()))
          .andExpect(jsonPath("$[0].iconType").value(responseDTO.getIconType()))
          .andExpect(jsonPath("$[0].latitude").value(responseDTO.getLatitude()))
          .andExpect(jsonPath("$[0].longitude").value(responseDTO.getLongitude()))
          .andExpect(jsonPath("$[0].startTime").value(startTime.toString()))
          .andExpect(jsonPath("$[0].endTime").value(endTime.toString()))
          .andExpect(jsonPath("$[0].radius").value(responseDTO.getRadius()))
          .andExpect(jsonPath("$[0].severity").value(responseDTO.getSeverity()));
    }

    @Test
    @DisplayName("GET /api/events with filters returns filtered events")
    void getFilteredEvents_ReturnsFilteredEvents() throws Exception {
      String filterName = "Earthquake";
      String filterStart = "2024-02-20T12:00:00";
      String filterEnd = "2024-02-20T14:00:00";

      LocalDateTime startTime = LocalDateTime.parse(filterStart);
      LocalDateTime endTime = LocalDateTime.parse(filterEnd);

      EventResponseDTO responseDTO = new EventResponseDTO();
      responseDTO.setId(1);
      responseDTO.setName("Earthquake in Oslo");
      responseDTO.setDescription("Moderate earthquake in Oslo area");
      responseDTO.setIconType(IconEnum.danger.toString());
      responseDTO.setLatitude(59.9139);
      responseDTO.setLongitude(10.7522);
      responseDTO.setStartTime(startTime.toString());
      responseDTO.setEndTime(endTime.toString());
      responseDTO.setRadius(5000);
      responseDTO.setSeverity(3);

      when(eventService.getFilteredEvents(
          eq(filterName),
          isNull(),
          eq(filterStart),
          eq(filterEnd),
          isNull()))
          .thenReturn(List.of(responseDTO));

      mvc.perform(get(BASE_URL)
          .param("name", filterName)
          .param("startTime", filterStart)
          .param("endTime", filterEnd))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].name").value("Earthquake in Oslo"))
          .andExpect(jsonPath("$[0].startTime").value(startTime.toString()))
          .andExpect(jsonPath("$[0].endTime").value(endTime.toString()))
          .andExpect(jsonPath("$[0].severity").value(3));

    }

    @Test
    void getEventById_ReturnsEvent_WhenExists() throws Exception {
      EventResponseDTO responseDTO = new EventResponseDTO();
      responseDTO.setId(1);
      responseDTO.setName("Flood in Bergen");
      responseDTO.setIconType(IconEnum.danger.toString());
      responseDTO.setStartTime(LocalDateTime.now().minusHours(1).toString());
      responseDTO.setEndTime(LocalDateTime.now().plusHours(2).toString());
  
      when(eventService.getEventById(1)).thenReturn(Optional.of(responseDTO));
  
      mvc.perform(get(BASE_URL + "/{id}", 1))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.name").value("Flood in Bergen"))
          .andExpect(jsonPath("$.iconType").value("danger"))
          .andExpect(jsonPath("$.startTime").exists())
          .andExpect(jsonPath("$.endTime").exists());
    }

    @Test
    void getEventById_ReturnsNotFound_WhenNotExists() throws Exception {
      when(eventService.getEventById(999)).thenReturn(Optional.empty());

      mvc.perform(get(BASE_URL + "/{id}", 999))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value("Event not found with id = 999"));
    }

    @Test
    @DisplayName("GET /api/events/name/{name} returns 200 with valid event")
    void getEventByName_ReturnsEvent_WhenExists() throws Exception {
      EventResponseDTO dto = new EventResponseDTO();
      dto.setId(1);
      dto.setName("Skogbrann i Trondheim");
      dto.setDescription("Major forest fire in Trondheim area");
      dto.setIconType(IconEnum.danger.toString());

      when(eventService.getEventByName("Skogbrann i Trondheim")).thenReturn(Optional.of(dto));

      mvc.perform(get(BASE_URL + "/name/{name}", "Skogbrann i Trondheim"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.name").value("Skogbrann i Trondheim"));

      verify(eventService).getEventByName("Skogbrann i Trondheim");
    }

    @Test
    @DisplayName("GET /api/events/name/{name} returns 404 for unknown event")
    void getEventByName_ReturnsNotFound_WhenMissing() throws Exception {
      when(eventService.getEventByName("Romveseninvasjon")).thenReturn(Optional.empty());

      mvc.perform(get(BASE_URL + "/name/{name}", "Romveseninvasjon"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value(
              "Event not found with name = Romveseninvasjon"));
    }

    @Test
    @DisplayName("GET /api/events/active returns 200 with active events")
    void getActiveEvents_ReturnsActiveEvents() throws Exception {
      LocalDateTime now = LocalDateTime.now();
      EventResponseDTO dto1 = new EventResponseDTO();
      dto1.setId(1);
      dto1.setName("Flom i Oslo");
      dto1.setStartTime(now.minusHours(2).toString());
      dto1.setEndTime(now.plusHours(2).toString());

      EventResponseDTO dto2 = new EventResponseDTO();
      dto2.setId(2);
      dto2.setName("Snøstorm i Bergen");
      dto2.setStartTime(now.minusHours(1).toString());
      dto2.setEndTime(now.plusHours(3).toString());

      when(eventService.getActiveEvents()).thenReturn(List.of(dto1, dto2));

      mvc.perform(get(BASE_URL + "/active"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].name").value("Flom i Oslo"))
          .andExpect(jsonPath("$[1].id").value(2))
          .andExpect(jsonPath("$[1].name").value("Snøstorm i Bergen"));
    }

    @Test
    @DisplayName("GET /api/events/active returns 204 with empty list when no active events")
    void getActiveEvents_ReturnsNoContent_WhenNoActiveEvents() throws Exception {
      when(eventService.getActiveEvents()).thenReturn(List.of());

      mvc.perform(get(BASE_URL + "/active")).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/events/icon{iconType} returns 200 with events of specific icon type")
    void getEventsByIconType_ReturnsEvents() throws Exception {
      String iconType = IconEnum.danger.toString();
      EventResponseDTO dto1 = new EventResponseDTO();
      dto1.setId(1);
      dto1.setName("Flom i Oslo");
      dto1.setIconType(iconType);

      EventResponseDTO dto2 = new EventResponseDTO();
      dto2.setId(2);
      dto2.setName("Snøstorm i Bergen");
      dto2.setIconType(iconType);

      when(eventService.getEventsByIconType(iconType)).thenReturn(List.of(dto1, dto2));

      mvc.perform(get(BASE_URL + "/icon/{iconType}", iconType))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].name").value("Flom i Oslo"))
          .andExpect(jsonPath("$[1].id").value(2))
          .andExpect(jsonPath("$[1].name").value("Snøstorm i Bergen"));
    }

    @Test
    @DisplayName("GET /api/events/affecting returns 200 with affecting events")
    void getEventsAffecting_ReturnsEvents_WhenLocationAffected() throws Exception {
      EventResponseDTO dto = new EventResponseDTO();
      dto.setId(1);
      dto.setName("Flom i Bergen");
      dto.setLatitude(60.39299);
      dto.setLongitude(5.32415);
      dto.setRadius(5000);
      dto.setStartTime(LocalDateTime.now().plusHours(1).toString());
      dto.setEndTime(LocalDateTime.now().plusHours(3).toString());
      when(eventService.getEventsAffectingLocation(60.39299, 5.32415)).thenReturn(List.of(dto));

      mvc.perform(get(BASE_URL + "/affecting")
          .param("lat", String.valueOf(60.39299))
          .param("lon", String.valueOf(5.32415)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].name").value("Flom i Bergen"))
          .andExpect(jsonPath("$[0].latitude").value(60.39299))
          .andExpect(jsonPath("$[0].radius").value(5000));

      verify(eventService).getEventsAffectingLocation(60.39299, 5.32415);
    }

    @Test
    @DisplayName("GET /api/events/affecting returns 204 with no events when location not affected")
    void getEventsAffecting_ReturnsNoContent_WhenLocationNotAffected() throws Exception {
      when(eventService.getEventsAffectingLocation(60.39299, 5.32415)).thenReturn(List.of());

      mvc.perform(get(BASE_URL + "/affecting")
          .param("lat", String.valueOf(60.39299))
          .param("lon", String.valueOf(5.32415)))
          .andExpect(status().isNoContent());

      verify(eventService).getEventsAffectingLocation(60.39299, 5.32415);
    }

    @Test
    @DisplayName(("POST /api/events creates a new event and returns it"))
    void createEvent_ReturnsCreatedEvent() throws Exception {
      EventRequestDTO requestDTO = new EventRequestDTO();
      requestDTO.setName("Flom i Trondheim");
      requestDTO.setDescription("Severe flood in Trondheim area");
      requestDTO.setIconType(IconEnum.danger.toString());
      requestDTO.setLatitude(63.4305);
      requestDTO.setLongitude(10.3951);
      requestDTO.setStartTime(LocalDateTime.now().toString());
      requestDTO.setEndTime(LocalDateTime.now().plusHours(2).toString());
      requestDTO.setRadius(10000);
      requestDTO.setSeverity(4);

      EventResponseDTO responseDTO = new EventResponseDTO();
      responseDTO.setId(1);
      responseDTO.setName(requestDTO.getName());
      responseDTO.setDescription(requestDTO.getDescription());
      responseDTO.setIconType(requestDTO.getIconType());
      responseDTO.setLatitude(requestDTO.getLatitude());
      responseDTO.setLongitude(requestDTO.getLongitude());
      responseDTO.setStartTime(requestDTO.getStartTime());
      responseDTO.setEndTime(requestDTO.getEndTime());
      responseDTO.setRadius(requestDTO.getRadius());
      responseDTO.setSeverity(requestDTO.getSeverity());

      when(eventService.saveEvent(requestDTO)).thenReturn(responseDTO);

      mvc.perform(post(BASE_URL)
          .contentType("application/json")
          .content(mapper.writeValueAsString(requestDTO)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.name").value("Flom i Trondheim"))
          .andExpect(jsonPath("$.description").value("Severe flood in Trondheim area"));
    }

    @Test
    @DisplayName("PUT /api/events/{id} updates an existing event and returns it")
    void updateEvent_ReturnsUpdatedEvent() throws Exception {
      EventRequestDTO requestDTO = new EventRequestDTO();
      requestDTO.setName("Flom i Trondheim");
      requestDTO.setDescription("Severe flood in Trondheim area");
      requestDTO.setIconType(IconEnum.danger.toString());
      requestDTO.setLatitude(63.4305);
      requestDTO.setLongitude(10.3951);
      requestDTO.setStartTime(LocalDateTime.now().toString());
      requestDTO.setEndTime(LocalDateTime.now().plusHours(2).toString());
      requestDTO.setRadius(10000);
      requestDTO.setSeverity(4);

      EventResponseDTO responseDTO = new EventResponseDTO();
      responseDTO.setId(1);
      responseDTO.setName(requestDTO.getName());
      responseDTO.setDescription(requestDTO.getDescription());
      responseDTO.setIconType(requestDTO.getIconType());
      responseDTO.setLatitude(requestDTO.getLatitude());
      responseDTO.setLongitude(requestDTO.getLongitude());
      responseDTO.setStartTime(requestDTO.getStartTime());
      responseDTO.setEndTime(requestDTO.getEndTime());
      responseDTO.setRadius(requestDTO.getRadius());
      responseDTO.setSeverity(requestDTO.getSeverity());

      when(eventService.updateEvent(eq(1), eq(requestDTO))).thenReturn(responseDTO);

      mvc.perform(put(BASE_URL + "/{id}", 1)
          .contentType("application/json")
          .content(mapper.writeValueAsString(requestDTO)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.name").value("Flom i Trondheim"))
          .andExpect(jsonPath("$.description").value("Severe flood in Trondheim area"));
    }

    @Test
    @DisplayName("DELETE /api/events/{id} deletes an existing event")
    void deleteEvent_ReturnsNoContent() throws Exception {
      when(eventRepository.existsById(1)).thenReturn(true);
      mvc.perform(delete(BASE_URL + "/{id}", 1)).andExpect(status().isNoContent());

      verify(eventService).deleteEvent(1);
    }

    @Test
    @DisplayName("DELETE /api/events/{id} returns 404 when event not found")
    void deleteEvent_ReturnsNotFound() throws Exception {
      when(eventRepository.existsById(999)).thenReturn(false);
      mvc.perform(delete(BASE_URL + "/{id}", 999))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value("Event not found with id = 999"));
    }
  }

  @Nested
  @DisplayName("Negative test cases")
  class NegativeTests {
    @Test
    @DisplayName("GET /api/events/{id} returns 500 when an unexpected error occurs")
    void getEventById_ReturnsInternalError() throws Exception {
      when(eventService.getEventById(1)).thenThrow(new RuntimeException("An unexpected error occurred"));

      mvc.perform(get(BASE_URL + "/{id}", 1))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    @Test
    @DisplayName("GET /api/events/name/{name} returns 500 on service failure")
    void getEventByName_ReturnsInternalError_OnFailure() throws Exception {
      String errorMessage = "An unexpected error occurred";
      when(eventService.getEventByName("Feilet event")).thenThrow(new RuntimeException(errorMessage));

      mvc.perform(get(BASE_URL + "/name/{name}", "Feilet event"))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.message").value(errorMessage));

      verify(eventService).getEventByName("Feilet event");
    }

    @Test
    @DisplayName("POST /api/events returns 400 when event is invalid")
    void createEvent_ReturnsBadRequest_WhenInvalid() throws Exception {
      EventRequestDTO requestDTO = new EventRequestDTO();
      requestDTO.setName(null); // Invalid event

      when(eventService.saveEvent(requestDTO)).thenThrow(new RuntimeException("Validation failed"));

      mvc.perform(post(BASE_URL)
          .contentType("application/json")
          .content(mapper.writeValueAsString(requestDTO)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Validation failed"));
    }
    
    @Test
    @DisplayName("PUT /api/events/{id} returns 400 when event parameters are invalid")
    void updateEvent_ReturnsBadRequest_WhenInvalid() throws Exception {
      EventRequestDTO requestDTO = new EventRequestDTO();
      requestDTO.setName(null); // Invalid event

      when(eventService.updateEvent(eq(1), eq(requestDTO))).thenThrow(new RuntimeException("Validation failed"));

      mvc.perform(put(BASE_URL + "/{id}", 1)
          .contentType("application/json")
          .content(mapper.writeValueAsString(requestDTO)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Validation failed"));
    }
  }
}
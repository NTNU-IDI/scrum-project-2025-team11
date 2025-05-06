package no.ntnu.idatt2106.krisefikser.controller;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.PointOfInterestMapper;
import no.ntnu.idatt2106.krisefikser.repository.PointOfInterestRepository;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;
import no.ntnu.idatt2106.krisefikser.security.JwtAuthFilter;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.PointOfInterestService;
import no.ntnu.idatt2106.krisefikser.service.UserService;
import no.ntnu.idatt2106.krisefikser.model.Enums;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(PointOfInterestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PointOfInterestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private PointOfInterestService pointOfInterestService;

    @MockitoBean
    PointOfInterestRepository pointOfInterestRepository;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/interest";

    @Nested
    @DisplayName("Positive test cases")
    class PositiveTestCases {
        @Test
        @DisplayName("Get all points of interest")
        void getAllPointsOfInterest() throws Exception {
            PointOfInterestResponseDTO pointOfInterest = new PointOfInterestResponseDTO();
            pointOfInterest.setId(1);
            pointOfInterest.setName("Test Point");
            pointOfInterest.setDescription("Test Description");
            pointOfInterest.setIconType(Enums.IconEnum.medical);
            pointOfInterest.setLatitude(54.0);
            pointOfInterest.setLongitude(10.0);
            
            when(pointOfInterestService.findAll())
                    .thenReturn(List.of(pointOfInterest));
            
            mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pointOfInterest.getId()))
                .andExpect(jsonPath("$[0].name").value(pointOfInterest.getName()))
                .andExpect(jsonPath("$[0].description").value(pointOfInterest.getDescription()))
                .andExpect(jsonPath("$[0].iconType").value(pointOfInterest.getIconType().toString()))
                .andExpect(jsonPath("$[0].latitude").value(pointOfInterest.getLatitude()))
                .andExpect(jsonPath("$[0].longitude").value(pointOfInterest.getLongitude()));
        }

        @Test
        @DisplayName("Get point of interest by ID")
        void getPointOfInterestById() throws Exception {
            PointOfInterest pointOfInterest = new PointOfInterest();
            pointOfInterest.setId(1);
            pointOfInterest.setName("Test Point");
            pointOfInterest.setDescription("Test Description");
            pointOfInterest.setIconType(Enums.IconEnum.medical);
            pointOfInterest.setLatitude(54.0);
            pointOfInterest.setLongitude(10.0);

            when(pointOfInterestService.findById(1)).thenReturn(Optional.of(pointOfInterest));

            mockMvc.perform(get(BASE_URL + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pointOfInterest.getId()))
                .andExpect(jsonPath("$.name").value(pointOfInterest.getName()))
                .andExpect(jsonPath("$.description").value(pointOfInterest.getDescription()))
                .andExpect(jsonPath("$.iconType").value(pointOfInterest.getIconType().toString()))
                .andExpect(jsonPath("$.latitude").value(pointOfInterest.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(pointOfInterest.getLongitude()));
        }

        @Test
        @DisplayName("Get points of interest by icon type")
        void getPointsOfInterestByIconType() throws Exception {
            PointOfInterestResponseDTO pointOfInterest = new PointOfInterestResponseDTO();
            pointOfInterest.setId(1);
            pointOfInterest.setName("Test Point");
            pointOfInterest.setDescription("Test Description");
            pointOfInterest.setIconType(Enums.IconEnum.medical);
            pointOfInterest.setLatitude(54.0);
            pointOfInterest.setLongitude(10.0);

            PointOfInterestResponseDTO pointOfInterest2 = new PointOfInterestResponseDTO();
            pointOfInterest2.setId(2);
            pointOfInterest2.setName("Test Point 2");
            pointOfInterest2.setDescription("Test Description 2");
            pointOfInterest2.setIconType(Enums.IconEnum.medical);
            pointOfInterest2.setLatitude(55.0);
            pointOfInterest2.setLongitude(11.0);

            when(pointOfInterestService.findByIconType(List.of("medical")))
                    .thenReturn(List.of(pointOfInterest, pointOfInterest2));
            mockMvc.perform(get(BASE_URL + "/iconTypes?iconType=medical"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pointOfInterest.getId()))
                .andExpect(jsonPath("$[0].name").value(pointOfInterest.getName()))
                .andExpect(jsonPath("$[0].description").value(pointOfInterest.getDescription()))
                .andExpect(jsonPath("$[0].iconType").value(pointOfInterest.getIconType().toString()))
                .andExpect(jsonPath("$[0].latitude").value(pointOfInterest.getLatitude()))
                .andExpect(jsonPath("$[0].longitude").value(pointOfInterest.getLongitude()));
            mockMvc.perform(get(BASE_URL + "/iconTypes?iconType=medical"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id").value(pointOfInterest2.getId()))
                .andExpect(jsonPath("$[1].name").value(pointOfInterest2.getName()))
                .andExpect(jsonPath("$[1].description").value(pointOfInterest2.getDescription()))
                .andExpect(jsonPath("$[1].iconType").value(pointOfInterest2.getIconType().toString()))
                .andExpect(jsonPath("$[1].latitude").value(pointOfInterest2.getLatitude()))
                .andExpect(jsonPath("$[1].longitude").value(pointOfInterest2.getLongitude()));

        }

        @Test
        @DisplayName("Create a new point of interest")
        void createPointOfInterest() throws Exception {
            PointOfInterestRequestDTO pointOfInterest = new PointOfInterestRequestDTO();
            pointOfInterest.setName("Test Point");
            pointOfInterest.setDescription("Test Description");
            pointOfInterest.setIconType(Enums.IconEnum.medical);
            pointOfInterest.setLatitude(54.0);
            pointOfInterest.setLongitude(10.0);

            PointOfInterest pointOfInterestEntity = new PointOfInterest();
            pointOfInterestEntity.setName(pointOfInterest.getName());
            pointOfInterestEntity.setDescription(pointOfInterest.getDescription());
            pointOfInterestEntity.setIconType(pointOfInterest.getIconType());
            pointOfInterestEntity.setLatitude(pointOfInterest.getLatitude());
            pointOfInterestEntity.setLongitude(pointOfInterest.getLongitude());

            when(pointOfInterestService.save(pointOfInterest)).thenReturn(PointOfInterestMapper.toResponseDTO(pointOfInterestEntity));

            mockMvc.perform(post(BASE_URL)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(pointOfInterest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(pointOfInterest.getName()))
                .andExpect(jsonPath("$.description").value(pointOfInterest.getDescription()))
                .andExpect(jsonPath("$.iconType").value(pointOfInterest.getIconType().toString()))
                .andExpect(jsonPath("$.latitude").value(pointOfInterest.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(pointOfInterest.getLongitude()));
        }
    }
    
    @Nested
    @DisplayName("Negative test cases")
    class NegativeTestCases {
        @Test
        @DisplayName("Get point of interest by ID - Not Found")
        void getPointOfInterestByIdNotFound() throws Exception {
            when(pointOfInterestService.findById(1)).thenReturn(Optional.empty());

            mockMvc.perform(get(BASE_URL + "/{id}", 1))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Get points of interest by icon type - Not Found")
        void getPointsOfInterestByIconTypeNotFound() throws Exception {
            when(pointOfInterestService.findByIconType(List.of("medical"))).thenReturn(List.of());

            mockMvc.perform(get(BASE_URL + "/iconTypes?iconType=medical"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Create a new point of interest - Bad Request")
        void createPointOfInterestBadRequest() throws Exception {
            PointOfInterestRequestDTO pointOfInterest = new PointOfInterestRequestDTO();
            pointOfInterest.setName(null); // Invalid input

            mockMvc.perform(post(BASE_URL)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(pointOfInterest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Get all points of interest - Not Found")
        void getAllPointsOfInterestNotFound() throws Exception {
            when(pointOfInterestService.findAll()).thenReturn(List.of());

            mockMvc.perform(get(BASE_URL))
                .andExpect(status().isNotFound());
        }
    }

    
}

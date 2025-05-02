package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.ntnu.idatt2106.krisefikser.dto.*;
import no.ntnu.idatt2106.krisefikser.service.HouseholdService;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class HouseholdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HouseholdService householdService;

    @InjectMocks
    private HouseholdController householdController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(householdController).build();
    }

    @Test
    void testCreateHousehold() throws Exception {
        // Prepare request DTO with nested Address
        AddressRequestDTO addressDTO = new AddressRequestDTO();
        addressDTO.setStreet("Tors veg 24");
        addressDTO.setCity("Trondheim");
        addressDTO.setPostalCode("7035");
        addressDTO.setLongitude(65.10);
        addressDTO.setLatitude(75.10);

        HouseholdRequestDTO requestDTO = new HouseholdRequestDTO();
        requestDTO.setName("Jonas' hus");
        requestDTO.setMemberCount(1);
        requestDTO.setAddress(addressDTO);

        // Mock the service response
        HouseholdResponseDTO responseDTO = new HouseholdResponseDTO();
        responseDTO.setId(123);
        responseDTO.setName("Jonas' hus");
        responseDTO.setMemberCount(1);

        when(householdService.save(any(HouseholdRequestDTO.class))).thenReturn(responseDTO);

        // Perform the request
        mockMvc.perform(post("/api/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.name").value("Jonas' hus"))
                .andExpect(jsonPath("$.memberCount").value(1));
    }

    @Test
    void testUpdateHousehold() throws Exception {
        int householdId = 123;

        HouseholdUpdateDTO updateDTO = new HouseholdUpdateDTO();
        updateDTO.setName("Oppdatert hus");
        updateDTO.setMemberCount(3);

        HouseholdResponseDTO responseDTO = new HouseholdResponseDTO();
        responseDTO.setId(householdId);
        responseDTO.setName("Oppdatert hus");
        responseDTO.setMemberCount(3);

        when(householdService.existsById(householdId)).thenReturn(true);
        when(householdService.updateHousehold(any(Integer.class), any(HouseholdUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/household/{id}", householdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(householdId))
                .andExpect(jsonPath("$.name").value("Oppdatert hus"))
                .andExpect(jsonPath("$.memberCount").value(3));
    }

    @Test
    void testUpdateHousehold_NotFound() throws Exception {
        int nonExistentId = 999;

        HouseholdUpdateDTO updateDTO = new HouseholdUpdateDTO();
        updateDTO.setName("Doesn't matter");
        updateDTO.setMemberCount(2);

        when(householdService.existsById(nonExistentId)).thenReturn(false);

        mockMvc.perform(put("/api/household/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }
}

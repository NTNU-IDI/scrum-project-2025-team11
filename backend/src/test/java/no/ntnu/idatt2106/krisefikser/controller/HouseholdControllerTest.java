package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.dto.*;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.security.JwtAuthFilter;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.HouseholdInviteCodeService;
import no.ntnu.idatt2106.krisefikser.service.HouseholdService;
import no.ntnu.idatt2106.krisefikser.service.UserService;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(HouseholdController.class)
@AutoConfigureMockMvc(addFilters = false)
class HouseholdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter filter;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private HouseholdService householdService;

    @MockitoBean
    private HouseholdInviteCodeService householdInviteCodeService;

    @InjectMocks
    private HouseholdController householdController;

    @Autowired
    private ObjectMapper objectMapper;
    
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

    @WithMockUser(username = "testUser")
    @Test
    void testUpdateHousehold() throws Exception {
        int householdId = 123;

        HouseholdUpdateDTO updateDTO = new HouseholdUpdateDTO();
        updateDTO.setName("Hus");
        updateDTO.setMemberCount(3);

        HouseholdResponseDTO responseDTO = new HouseholdResponseDTO();
        responseDTO.setId(householdId);
        responseDTO.setName("Oppdatert hus");
        responseDTO.setMemberCount(3);

        User mockUser = new User();
        mockUser.setUsername("testUser");
        Household hh = new Household();
        hh.setId(householdId);
        mockUser.setHousehold(hh);
        when(userService.getUserByUsername("testUser"))
                .thenReturn(Optional.of(mockUser));

        when(householdService.existsById(householdId)).thenReturn(true);
        when(householdService.updateHousehold(eq(householdId), any()))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/household/update")
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

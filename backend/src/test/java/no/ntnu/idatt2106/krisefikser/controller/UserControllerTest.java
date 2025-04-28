package no.ntnu.idatt2106.krisefikser.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserRequest;
import no.ntnu.idatt2106.krisefikser.dto.UserResponse;
import no.ntnu.idatt2106.krisefikser.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Create user")
    void testCreateUser() throws Exception {
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

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("Jonas");
        userRequest.setEmail("jon@mail.com");
        userRequest.setPassword("password");
        userRequest.setFirstName("Jonas");
        userRequest.setLastName("Jonassen");
        userRequest.setHouseholdId(123); // Assuming the household ID is 123

        // Mock the service response
        UserResponse responseDTO = new UserResponse();
        responseDTO.setUsername("Jonas");
        responseDTO.setEmail("jon@mail.com");
        responseDTO.setFirstName("Jonas");
        responseDTO.setLastName("Jonassen");
        responseDTO.setHouseholdId(123); // Assuming the household ID is 123

        when(userService.saveUser(any(UserRequest.class))).thenReturn(responseDTO);

        // Perform the request
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Jonas"))
                .andExpect(jsonPath("$.email").value("jon@mail.com"))
                .andExpect(jsonPath("$.firstName").value("Jonas"))
                .andExpect(jsonPath("$.lastName").value("Jonassen"))
                .andExpect(jsonPath("$.householdId").value(123));
                
    }

    
}

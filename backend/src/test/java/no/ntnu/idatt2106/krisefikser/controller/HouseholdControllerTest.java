/* package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.ntnu.idatt2106.krisefikser.service.HouseholdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class HouseholdControllerTest {
    private MockMvc mockMvc;

    @Mock
    private HouseholdService householdService;

    @InjectMocks
    private HouseholdController householdController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(householdController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateHousehold() throws Exception {
        Household household = new Household();
        household.setMemberCount(1);
        household.setName("Jonas' hus");
        Address address = new Address();
        address.setCity("Trondheim");
        address.setId(1);
        address.setPostalCode("7035");
        address.setStreet("Tors veg 24");
        address.setLongitude(65.10);
        address.setLatitude(75.10);
        household.setAddress(address);

        when(householdService.save(any(Household.class))).thenReturn(household);

        mockMvc.perform(post("/api/household")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(household)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jonas' hus"));


    }
}
 */
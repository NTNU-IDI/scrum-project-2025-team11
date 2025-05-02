package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class HouseholdControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /*@BeforeEach
    void setup() {
        householdRepository.deleteAll(); // Clean DB before each test
    }*/

    @Test
    void testCreateAndGetHousehold() throws Exception {
        AddressRequestDTO addressDTO = new AddressRequestDTO();
        addressDTO.setStreet("Integration Street 1");
        addressDTO.setCity("Integrasjonsby");
        addressDTO.setPostalCode("1234");
        addressDTO.setLatitude(63.4305);
        addressDTO.setLongitude(10.3951);

        HouseholdRequestDTO householdRequestDTO = new HouseholdRequestDTO();
        householdRequestDTO.setName("Integration Home");
        householdRequestDTO.setMemberCount(4);
        householdRequestDTO.setAddress(addressDTO);

        // Create Household
        String responseJson = mockMvc.perform(post("/api/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(householdRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Integration Home"))
                .andExpect(jsonPath("$.memberCount").value(4))
                .andReturn().getResponse().getContentAsString();

        // Extract ID
        int createdId = objectMapper.readTree(responseJson).get("id").asInt();

        // Fetch household by ID
        mockMvc.perform(get("/api/household/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.name").value("Integration Home"))
                .andExpect(jsonPath("$.memberCount").value(4));

        // Verify in database
        Household householdInDb = householdRepository.findById(createdId).orElse(null);
        assertThat(householdInDb).isNotNull();
        assertThat(householdInDb.getName()).isEqualTo("Integration Home");
    }

    @Test
    @WithMockUser
    void testUpdateHousehold() throws Exception {

        Household initialHousehold = new Household();
        initialHousehold.setName("Original Home");
        initialHousehold.setMemberCount(2);
        initialHousehold = householdRepository.save(initialHousehold);

        int id = initialHousehold.getId();


        String updateJson = """
        {
            "name": "Updated Home",
            "memberCount": 5
        }
        """;

        // Step 3: Perform PUT /api/household/{id}
        mockMvc.perform(put("/api/household/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated Home"))
                .andExpect(jsonPath("$.memberCount").value(5));

        // Step 4: Verify in database
        Household updatedHousehold = householdRepository.findById(id).orElseThrow();
        assertThat(updatedHousehold.getName()).isEqualTo("Updated Home");
        assertThat(updatedHousehold.getMemberCount()).isEqualTo(5);
    }


    /*@Test
    void testDeleteHousehold() throws Exception {
        // First insert a household manually
        Household household = new Household();
        household.setName("Delete Me");
        household.setMemberCount(2);
        household = householdRepository.save(household); // Save to DB

        int id = household.getId();

        // Verify it exists
        assertThat(householdRepository.existsById(id)).isTrue();

        // Delete household
        mockMvc.perform(delete("/api/household/{id}", id))
                .andExpect(status().isNoContent());

        // Verify it's gone
        assertThat(householdRepository.existsById(id)).isFalse();
    }*/
}

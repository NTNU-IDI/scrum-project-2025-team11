package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdRepository;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@AutoConfigureMockMvc(addFilters = false) 
@Import(TestSecurityConfig.class)
class HouseholdControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    User testUser;

    @BeforeEach
    @DisplayName("Set up the database before each test")
    void setUp() {
        testUser = new User();
        testUser.setUsername("erling-b");
        testUser.setEmail("brauten@gmail.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Erling");
        testUser.setLastName("Braut");
    }
    
    @AfterEach
    @DisplayName("Clean up the database after each test")
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "erling-b", roles = "USER")
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

        // Extract household ID
        int hhid = objectMapper.readTree(responseJson).get("id").asInt();

        // Create & save a User "erling-b" who belongs to that household
        Household hh = householdRepository.findById(hhid).orElseThrow();
        testUser.setHousehold(hh);
        userRepository.save(testUser);

        // Fetch household by ID
        mockMvc.perform(get("/api/household/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hhid))
                .andExpect(jsonPath("$.name").value("Integration Home"))
                .andExpect(jsonPath("$.memberCount").value(4));

        // Verify in database
        Household householdInDb = householdRepository.findById(hhid).orElse(null);
        assertThat(householdInDb).isNotNull();
        assertThat(householdInDb.getName()).isEqualTo("Integration Home");
    }

    @Test
    @WithMockUser(username = "erling-b")
    void testUpdateHousehold() throws Exception {
      // --- 1) Preload an existing household in DB ---
      Household initial = new Household();
      initial.setName("Original Home");
      initial.setMemberCount(2);
      initial = householdRepository.save(initial);
  
      // --- 2) Create user “bob” → that household ---
      testUser.setHousehold(initial);
      userRepository.save(testUser);
  
      // --- 3) Build the JSON update payload ---
      String updateJson = """
        {
          "name": "Updated Home",
          "memberCount": 5
        }
      """;
  
      // --- 4) PUT /api/household/update  (no {id}) ---
      mockMvc.perform(put("/api/household/update")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(updateJson))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").value(initial.getId()))
             .andExpect(jsonPath("$.name").value("Updated Home"))
             .andExpect(jsonPath("$.memberCount").value(5));
  
      // verify in the DB 
      Household updated = householdRepository.findById(initial.getId()).orElseThrow();
      assertThat(updated.getName()).isEqualTo("Updated Home");
      assertThat(updated.getMemberCount()).isEqualTo(5);
    }
}

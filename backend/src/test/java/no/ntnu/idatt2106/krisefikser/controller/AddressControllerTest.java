package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.security.JwtAuthFilter;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.AddressService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false) 
public class AddressControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper; 

  @MockitoBean 
  private JwtUtil jwtUtil;           

  @MockitoBean 
  private JwtAuthFilter jwtAuthFilter;

  @MockitoBean
  private AddressService addressService;

  @MockitoBean
  private UserService userService;

  @Nested
  @DisplayName("Positive test cases")
  class PositiveTests {

    @Test
    @DisplayName("GET /api/addresses returns list of addresses")
    void getAllAddresses() throws Exception {
      AddressResponseDTO address1 = new AddressResponseDTO();
      address1.setId(1);
      address1.setStreet("Tors veg 24");
      address1.setPostalCode("7035");
      address1.setCity("Trondheim");
      address1.setLatitude(65.10);
      address1.setLongitude(75.10);

      AddressResponseDTO address2 = new AddressResponseDTO();
      address2.setId(2);
      address2.setStreet("Storgata 1");
      address2.setPostalCode("7011");
      address2.setCity("Trondheim");
      address2.setLatitude(63.42);
      address2.setLongitude(10.39);

      when(addressService.findAllAddresses()).thenReturn(List.of(address1, address2));

      mvc.perform(get("/api/addresses"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].street").value("Tors veg 24"))
          .andExpect(jsonPath("$[1].id").value(2))
          .andExpect(jsonPath("$[1].street").value("Storgata 1"));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("GET /api/addresses/me returns the logged-in user's address")
    void getMyAddress_ReturnsAddressOfLoggedInUser() throws Exception {
      Address address = new Address();
      address.setId(1);
      address.setStreet("Tors veg 24");
      address.setPostalCode("7035");
      address.setCity("Trondheim");
      address.setLatitude(65.10);
      address.setLongitude(75.10);

      Household hh = new Household();
      hh.setAddress(address);

      User mockUser = new User();
      mockUser.setUsername("testUser");
      mockUser.setHousehold(hh);

      when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(mockUser));

      mvc.perform(get("/api/addresses/me"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.street").value("Tors veg 24"))
          .andExpect(jsonPath("$.postalCode").value("7035"))
          .andExpect(jsonPath("$.latitude").value(65.10))
          .andExpect(jsonPath("$.longitude").value(75.10));
    }

    @Test
    @DisplayName("POST /api/addresses creates and returns the new address")
    void createAddress() throws Exception {
      AddressRequestDTO address = new AddressRequestDTO();
      address.setStreet("Tors veg 24");
      address.setPostalCode("7035");
      address.setCity("Trondheim");
      address.setLatitude(65.10);
      address.setLongitude(75.10);

      AddressResponseDTO savedAddress = new AddressResponseDTO();
      savedAddress.setId(1);
      savedAddress.setStreet("Tors veg 24");
      savedAddress.setPostalCode("7035");
      savedAddress.setCity("Trondheim");
      savedAddress.setLatitude(65.10);
      savedAddress.setLongitude(75.10);

        
      when(addressService.save(any(AddressRequestDTO.class))).thenReturn(savedAddress);

      mvc.perform(post("/api/addresses")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(address)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.street").value("Tors veg 24"));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("PUT /api/addresses updates and returns the user's address")
    void updateMyAddress_ReturnsUpdatedAddress() throws Exception {
      AddressRequestDTO request = new AddressRequestDTO();
      request.setStreet("Tors veg 24");
      request.setPostalCode("7035");
      request.setCity("Trondheim");
      request.setLatitude(65.10);
      request.setLongitude(75.10);
  
      AddressResponseDTO response = new AddressResponseDTO();
      response.setId(1);
      response.setStreet("Oppdatert veg 24");
      response.setPostalCode("9999");
      response.setCity("Grimstad");
      response.setLatitude(16.10);
      response.setLongitude(98.10);
  
      // Build a User → Household → Address with id=1
      Address current = new Address();
      current.setId(1);
      Household hh = new Household();
      hh.setAddress(current);
      User mockUser = new User();
      mockUser.setUsername("testUser");
      mockUser.setHousehold(hh);
  
      when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(mockUser));
  
      // Stub addressService.updateAddress(1, request) → response
      when(addressService.updateAddress(eq(1), any(AddressRequestDTO.class))).thenReturn(response);
  
      mvc.perform(put("/api/addresses")
              .contentType(MediaType.APPLICATION_JSON)
              .content(mapper.writeValueAsString(request)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id").value(1))
         .andExpect(jsonPath("$.street").value("Oppdatert veg 24"))
         .andExpect(jsonPath("$.postalCode").value("9999"))
         .andExpect(jsonPath("$.city").value("Grimstad"))
         .andExpect(jsonPath("$.latitude").value(16.10))
         .andExpect(jsonPath("$.longitude").value(98.10));
    }

    @Test
    @DisplayName("DELETE /api/addresses/{id} deletes address")
    void deleteAddress() throws Exception {
      mvc.perform(delete("/api/addresses/1"))
          .andExpect(status().isNoContent());
      verify(addressService).deleteById(1);
    }
  }

  @Nested
  @DisplayName("Negative test cases")
  class NegativeTests {

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("GET /api/addresses/me returns 404 when user has no address")
    void getMyAddress_Returns404WhenNoAddress() throws Exception {
      Household hh = new Household();
      hh.setAddress(null);
      User mockUser = new User();
      mockUser.setUsername("testUser");
      mockUser.setHousehold(hh);

      when(userService.getUserByUsername("testUser"))
          .thenReturn(Optional.of(mockUser));

      mvc.perform(get("/api/addresses/me"))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/addresses returns 400 when address is invalid")
    void createAddressInvalid() throws Exception {
      AddressRequestDTO address = new AddressRequestDTO();
      address.setStreet(null); // Invalid address street

      when(addressService.save(any(AddressRequestDTO.class))).thenThrow(new RuntimeException("Invalid address"));

      mvc.perform(post("/api/addresses")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(address)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("PUT /api/addresses returns 404 when address not found")
    void updateMyAddress_Returns404WhenNotFound() throws Exception {
      AddressRequestDTO request = new AddressRequestDTO();
      request.setStreet("Tors veg 24");

      // user with an address id that doesn’t exist
      Address current = new Address();
      current.setId(1);
      Household hh = new Household();
      hh.setAddress(current);
      User mockUser = new User();
      mockUser.setHousehold(hh);
      mockUser.setUsername("testUser");
      when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(mockUser));

      when(addressService.updateAddress(eq(1), any(AddressRequestDTO.class))).thenThrow(new RuntimeException("Address not found"));

      mvc.perform(put("/api/addresses")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(request)))
          .andExpect(status().isNotFound())
          .andExpect(header().string("Error message", "Address not found"));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("PUT /api/addresses returns 400 when address parameters are invalid")
    void updateMyAddress_Returns400WhenInvalid() throws Exception {
      AddressRequestDTO request = new AddressRequestDTO();
      request.setStreet(null); // Invalid street

      Address current = new Address();
      current.setId(1);
      current.setStreet("Tors veg 24");
      Household hh = new Household();
      hh.setAddress(current);
      User mockUser = new User();
      mockUser.setHousehold(hh);
      mockUser.setUsername("testUser");
      when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(mockUser));

      when(addressService.updateAddress(eq(1), any(AddressRequestDTO.class))).thenThrow(new Exception("Invalid address"));

      mvc.perform(put("/api/addresses")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(header().string("Error message", "Invalid address"));
    }

    @Test
    @DisplayName("DELETE /api/addresses/{id} returns 404 when address not found")
    void deleteAddressNotFound() throws Exception {
      doThrow(new RuntimeException("Address not found")).when(addressService).deleteById(1);

      mvc.perform(delete("/api/addresses/1"))
          .andExpect(status().isNotFound());
    }
  }
  
}

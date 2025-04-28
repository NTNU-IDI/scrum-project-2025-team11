package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.service.AddressService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(AddressController.class) 
public class AddressControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper; // Use Spring's ObjectMapper bean

  @MockitoBean
  private AddressService addressService;

  @BeforeEach
  void setup() {
    mapper = new ObjectMapper();
  }

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
    @DisplayName("GET /api/addresses/{id} returns address by ID")
    void getAddressById() throws Exception {
      Address address = new Address();
      address.setId(1);
      address.setStreet("Tors veg 24");
      address.setPostalCode("7035");
      address.setCity("Trondheim");
      address.setLatitude(65.10);
      address.setLongitude(75.10);

      when(addressService.findById(1)).thenReturn(java.util.Optional.of(address));

      mvc.perform(get("/api/addresses/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/addresses creates and returns address")
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
    @DisplayName("PUT /api/addresses/{id} updates and returns address")
    void updateAddress() throws Exception {
      AddressRequestDTO address = new AddressRequestDTO();
      address.setStreet("Tors veg 24");
      address.setPostalCode("7035");
      address.setCity("Trondheim");
      address.setLatitude(65.10);
      address.setLongitude(75.10);

      AddressResponseDTO updatedAddress = new AddressResponseDTO();
      updatedAddress.setId(1);
      updatedAddress.setStreet("Oppdatert veg 24");
      updatedAddress.setPostalCode("9999");
      updatedAddress.setCity("Grimstad");
      updatedAddress.setLatitude(16.10);
      updatedAddress.setLongitude(98.10);

        
      when(addressService.updateAddress(eq(1), any(AddressRequestDTO.class))).thenReturn(updatedAddress);

      mvc.perform(put("/api/addresses/1")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(address)))
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
    @DisplayName("GET /api/addresses/{id} returns 404 when address not found")
    void getAddressByIdNotFound() throws Exception {
      when(addressService.findById(1)).thenReturn(java.util.Optional.empty());

      mvc.perform(get("/api/addresses/1"))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/addresses returns 400 when address is invalid")
    void createAddressInvalid() throws Exception {
      AddressRequestDTO address = new AddressRequestDTO();
      address.setStreet(null); // Invalid address

      when(addressService.save(any(AddressRequestDTO.class))).thenThrow(new RuntimeException("Invalid address"));

      mvc.perform(post("/api/addresses")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(address)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/addresses/{id} returns 404 when address not found")
    void updateAddressNotFound() throws Exception {
      AddressRequestDTO address = new AddressRequestDTO();
      address.setStreet("Tors veg 24");

      when(addressService.updateAddress(eq(1), any(AddressRequestDTO.class))).thenThrow(new RuntimeException("Address not found"));

      mvc.perform(put("/api/addresses/1")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(address)))
          .andExpect(status().isNotFound());
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

package no.ntnu.idatt2106.krisefikser.service;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.repository.AddressRepository;

public class AddressServiceTest {
  @Mock
  private AddressRepository addressRepository;

  @InjectMocks
  private AddressService addressService;

  private Address address;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }
  @BeforeEach
  void setup() {
    address = new Address();
    address.setId(1);
    address.setStreet("Test Street");
    address.setPostalCode("1234");
    address.setCity("Test City");
    address.setLatitude(10.0);
    address.setLongitude(20.0);
  }
  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {
    @Test
    void testFindById() {
      when(addressRepository.findById(1)).thenReturn(Optional.of(address));

      Optional<Address> foundAddress = addressService.findById(1);
      verify(addressRepository, times(1)).findById(1);
      assertNotNull(foundAddress);
      assertEquals(address.getId(), foundAddress.get().getId());
    }

    @Test
    void testCreateAddress() {
      AddressRequestDTO addressDTO = new AddressRequestDTO();
      addressDTO.setStreet("Test Street");
      addressDTO.setPostalCode("1234");
      addressDTO.setCity("Test City");
      addressDTO.setLatitude(10.0);
      addressDTO.setLongitude(20.0);

      AddressResponseDTO savedAddress = null;
      when(addressRepository.save(any(Address.class))).thenReturn(address);
      try {
        savedAddress = addressService.save(addressDTO);
      } catch (Exception e) {
        fail("Exception should not be thrown: " + e.getMessage());
      }

      verify(addressRepository, times(1)).save(any(Address.class));
      assertNotNull(savedAddress);
    }

    @Test
    void testUpdateAddress() {
      AddressRequestDTO addressDTO = new AddressRequestDTO();
      addressDTO.setStreet("Updated Street");
      addressDTO.setPostalCode("5678");
      addressDTO.setCity("Updated City");
      addressDTO.setLatitude(30.0);
      addressDTO.setLongitude(40.0);

      when(addressRepository.findById(1)).thenReturn(Optional.of(address));
      when(addressRepository.save(any(Address.class))).thenReturn(address);

      AddressResponseDTO updatedAddress = null;
      try {
        updatedAddress = addressService.updateAddress(1, addressDTO);
      } catch (Exception e) {
        fail("Exception should not be thrown: " + e.getMessage());
      }

      verify(addressRepository, times(1)).save(any(Address.class));
      verify(addressRepository, times(1)).findById(1);
      assertNotNull(updatedAddress);
      assertEquals(updatedAddress.getStreet(), address.getStreet());
    }

    @Test
    void testDeleteAddress() {
      when(addressRepository.existsById(1)).thenReturn(true);
      doNothing().when(addressRepository).deleteById(1);
      addressService.deleteById(1);
      verify(addressRepository, times(1)).deleteById(1);
    }

    @Test
    void findFindAllAddresses() {
      Address address2 = new Address();
      address2.setId(2);
      address2.setStreet("Test Street 2");
      address2.setPostalCode("5678");
      address2.setCity("Test City 2");
      address2.setLatitude(30.0);
      address2.setLongitude(40.0);

      when(addressRepository.findAll()).thenReturn(List.of(address, address2));
      List<AddressResponseDTO> addresses = addressService.findAllAddresses();
      verify(addressRepository, times(1)).findAll();

      assertNotNull(addresses);
      assertEquals(2, addresses.size());
      assertEquals(address.getId(), addresses.get(0).getId());
      assertEquals(address2.getId(), addresses.get(1).getId());
      assertEquals(address.getStreet(), addresses.get(0).getStreet());
      assertEquals(address2.getStreet(), addresses.get(1).getStreet());
    }
  }
  

}

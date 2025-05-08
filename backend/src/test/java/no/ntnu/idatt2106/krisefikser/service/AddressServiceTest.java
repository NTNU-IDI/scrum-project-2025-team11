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
import no.ntnu.idatt2106.krisefikser.mapper.AddressMapper;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.repository.AddressRepository;

public class AddressServiceTest {
  @Mock
  private AddressRepository addressRepository;

  @Mock
  private AddressMapper addressMapper;

  @InjectMocks
  private AddressService addressService;

  private Address address;
  private AddressResponseDTO addressResponseDTO;

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

    addressResponseDTO = new AddressResponseDTO();
    addressResponseDTO.setId(address.getId());
    addressResponseDTO.setStreet(address.getStreet());
    addressResponseDTO.setPostalCode(address.getPostalCode());
    addressResponseDTO.setCity(address.getCity());
    addressResponseDTO.setLatitude(address.getLatitude());
    addressResponseDTO.setLongitude(address.getLongitude());
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
    void testCreateAddress() throws Exception {
      AddressRequestDTO addressDTO = new AddressRequestDTO();
      addressDTO.setStreet("Test Street");
      addressDTO.setPostalCode("1234");
      addressDTO.setCity("Test City");
      addressDTO.setLatitude(10.0);
      addressDTO.setLongitude(20.0);

      when(addressMapper.toEntity(any(AddressRequestDTO.class))).thenReturn(address);
      when(addressRepository.save(any(Address.class))).thenReturn(address);
      when(addressMapper.toResponseDTO(any(Address.class))).thenReturn(addressResponseDTO);

      AddressResponseDTO createdAddress = addressService.save(addressDTO);

      verify(addressRepository, times(1)).save(any(Address.class));

      assertNotNull(createdAddress);
      assertEquals(address.getId(), createdAddress.getId());
      assertEquals(address.getStreet(), createdAddress.getStreet());
    }

    @Test
    void testUpdateAddress() throws Exception {
      AddressRequestDTO addressDTO = new AddressRequestDTO();
      addressDTO.setStreet("Updated Street");
      addressDTO.setPostalCode("5678");
      addressDTO.setCity("Updated City");
      addressDTO.setLatitude(30.0);
      addressDTO.setLongitude(40.0);

      when(addressRepository.findById(1)).thenReturn(Optional.of(address));
      when(addressMapper.toEntity(any(AddressRequestDTO.class))).thenReturn(address);
      when(addressRepository.save(any(Address.class))).thenReturn(address);
      when(addressMapper.toResponseDTO(any(Address.class))).thenAnswer(invocation -> {
        Address updatedAddress = invocation.getArgument(0);
        AddressResponseDTO responseDTO = new AddressResponseDTO();
        responseDTO.setId(updatedAddress.getId());
        responseDTO.setStreet(updatedAddress.getStreet());
        responseDTO.setPostalCode(updatedAddress.getPostalCode());
        responseDTO.setCity(updatedAddress.getCity());
        responseDTO.setLatitude(updatedAddress.getLatitude());
        responseDTO.setLongitude(updatedAddress.getLongitude());
        return responseDTO;
      });

      AddressResponseDTO updatedAddress = addressService.updateAddress(1, addressDTO);

      verify(addressRepository, times(1)).save(any(Address.class));
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

      List<Address> addresses = List.of(address, address2);

      when(addressRepository.findAll()).thenReturn(addresses);
      when(addressMapper.toResponseDTO(any(Address.class))).thenReturn(addressResponseDTO);

      assertNotNull(addresses);
      assertEquals(2, addresses.size());
      assertEquals(address.getId(), addresses.get(0).getId());
      assertEquals(address2.getId(), addresses.get(1).getId());
      assertEquals(address.getStreet(), addresses.get(0).getStreet());
      assertEquals(address2.getStreet(), addresses.get(1).getStreet());
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {
    @Test
    void testCreateAddressWithMissingFields() {
      AddressRequestDTO addressDTO = new AddressRequestDTO();
      addressDTO.setStreet(null); // Missing street

      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        addressService.save(addressDTO);
      });

      String expectedMessage = "Street, postal code, and/or city cannot be null";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateAddressNotFound() {
      AddressRequestDTO addressDTO = new AddressRequestDTO();
      addressDTO.setStreet("Updated Street");
      addressDTO.setPostalCode("5678");
      addressDTO.setCity("Updated City");

      when(addressRepository.findById(1)).thenReturn(Optional.empty());

      Exception exception = assertThrows(RuntimeException.class, () -> {
        addressService.updateAddress(1, addressDTO);
      });

      String expectedMessage = "Address not found with id: 1";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateAddressWithMissingFields() {
      AddressRequestDTO addressDTO = new AddressRequestDTO();
      addressDTO.setStreet(null); // Missing street

      when(addressRepository.findById(1)).thenReturn(Optional.of(address));

      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        addressService.updateAddress(1, addressDTO);
      });

      String expectedMessage = "Street, postal code, and city cannot be null";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void testDeleteAddressNotFound() {
      when(addressRepository.existsById(1)).thenReturn(false);

      Exception exception = assertThrows(RuntimeException.class, () -> {
        addressService.deleteById(1);
      });

      String expectedMessage = "Address not found with id: 1";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
    }
  }
  

}

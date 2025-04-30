package no.ntnu.idatt2106.krisefikser.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import no.ntnu.idatt2106.krisefikser.model.Address;

@ActiveProfiles("test")
@DataJpaTest
class AddressRepositoryTest {
  @Autowired
  private AddressRepository addressRepository;

  @Test
  void testCreateAndSaveAddress() {
    Address address = new Address();
    address.setStreet("Test gate");
    address.setCity("Test by");
    address.setPostalCode("1935");
    address.setLatitude(10.0);
    address.setLongitude(20.0);
    
    Address savedAddress = addressRepository.save(address);
    
    // then
    var foundAddress = addressRepository.findById(savedAddress.getId());
    assertTrue(foundAddress.isPresent());
    assertEquals("Test gate", foundAddress.get().getStreet());
    assertEquals("Test by", foundAddress.get().getCity());
    assertEquals("1935", foundAddress.get().getPostalCode());
    assertEquals(10.0, foundAddress.get().getLatitude());
    assertEquals(20.0, foundAddress.get().getLongitude());
  }

  @Test
  void testFindAddressById() {
    Address address = new Address();
    address.setStreet("Test gate");
    address.setCity("Test by");
    address.setPostalCode("1935");
    address.setLatitude(10.0);
    address.setLongitude(20.0);
    
    Address savedAddress = addressRepository.save(address);
    
    var foundAddress = addressRepository.findById(savedAddress.getId());
    
    assertTrue(foundAddress.isPresent());
    assertEquals(savedAddress.getId(), foundAddress.get().getId());
  }

  @Test
  void testDeleteAddress() {
    Address address = new Address();
    address.setStreet("Test gate");
    address.setCity("Test by");
    address.setPostalCode("1935");
    address.setLatitude(10.0);
    address.setLongitude(20.0);
    
    Address savedAddress = addressRepository.save(address);
    
    addressRepository.delete(savedAddress);
    
    var foundAddress = addressRepository.findById(savedAddress.getId());
    
    assertTrue(foundAddress.isEmpty());
  }
}

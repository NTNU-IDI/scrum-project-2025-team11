package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.repository.AddressRepository;

/**
 *  Service class for managing addresses in the system.
 * This class is responsible for handling business logic related to addresses, such as
 * creating, updating, deleting, and retrieving address information.
 */
@Service
@RequiredArgsConstructor
public class AddressService {
  private final AddressRepository addressRepository;

  // Add methods for creating, updating, deleting, and retrieving addresses here.

  public Optional<Address> findById(Long id) {
    return addressRepository.findById(id);
  }

  public Address save(Address address) {
    return addressRepository.save(address);
  }

  public Address updateAddress(Long id, Address updatedAddress) {
    Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
    if (updatedAddress.getStreet() != null) {
      existingAddress.setStreet(updatedAddress.getStreet());
    }
    if (updatedAddress.getPostalCode() != null) {
      existingAddress.setPostalCode(updatedAddress.getPostalCode());
    }
    if (updatedAddress.getCity() != null) {
      existingAddress.setCity(updatedAddress.getCity());
    }
    if (updatedAddress.getLatitude() != null) {
      existingAddress.setLatitude(updatedAddress.getLatitude());
    }
    if (updatedAddress.getLongitude() != null) {
      existingAddress.setLongitude(updatedAddress.getLongitude());
    }
    return addressRepository.save(existingAddress);
  }

  public void deleteById(Long id) {
    addressRepository.deleteById(id);
  }

  public boolean existsById(Long id) {
    return addressRepository.existsById(id);
  }

  public List<Address> findAll() {
    return addressRepository.findAll();
  }

  
}

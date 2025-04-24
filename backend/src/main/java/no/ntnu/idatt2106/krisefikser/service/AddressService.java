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

  public Optional<Address> findById(int id) {
    return addressRepository.findById(id);
  }

  /**
   * Saves a new address to the database.
   * @param address the address to save.
   * @return the saved address.
   * @throws Exception if the address is invalid or cannot be saved.
   */
  public Address save(Address address) throws Exception {
    if (address.getStreet() == null || address.getPostalCode() == null || address.getCity() == null) {
      throw new IllegalArgumentException("Street, postal code, and city cannot be null");
    }
    return addressRepository.save(address);
  }

  public Address updateAddress(int id, Address updatedAddress) {
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

  public void deleteById(int id) {
    addressRepository.deleteById(id);
  }

  public boolean existsById(int id) {
    return addressRepository.existsById(id);
  }

  public List<Address> findAll() {
    return addressRepository.findAll();
  }

  
}

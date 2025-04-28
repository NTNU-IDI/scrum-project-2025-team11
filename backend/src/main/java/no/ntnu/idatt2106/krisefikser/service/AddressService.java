package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
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

  private AddressResponseDTO mapToResponseDTO(Address address) {
    AddressResponseDTO dto = new AddressResponseDTO();
    dto.setId(address.getId());
    dto.setStreet(address.getStreet());
    dto.setPostalCode(address.getPostalCode());
    dto.setCity(address.getCity());
    dto.setLatitude(address.getLatitude());
    dto.setLongitude(address.getLongitude());
    return dto;
  }

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
  public AddressResponseDTO save(AddressRequestDTO addressDTO) throws Exception {
    if (addressDTO.getStreet() == null || addressDTO.getPostalCode() == null || addressDTO.getCity() == null) {
      throw new IllegalArgumentException("Street, postal code, and city cannot be null");
    }

    Address address = new Address();
    address.setStreet(addressDTO.getStreet());
    address.setPostalCode(addressDTO.getPostalCode());
    address.setCity(addressDTO.getCity());
    address.setLatitude(addressDTO.getLatitude());
    address.setLongitude(addressDTO.getLongitude());

    Address savedAddress = addressRepository.save(address);
    return mapToResponseDTO(savedAddress);
    
  }

  /**
   * Updates an existing address in the database.
   * @param id the ID of the address to update.
   * @param addressDTO the new address data.
   * @return the updated address.
   * @throws Exception if the address is not found or fields are invalid
   */
  public AddressResponseDTO updateAddress(int id, AddressRequestDTO addressDTO) throws Exception {
    if (!existsById(id)) {
      throw new RuntimeException("Address not found with id: " + id);
    }
    if (addressDTO.getStreet() == null || addressDTO.getPostalCode() == null || addressDTO.getCity() == null) {
      throw new IllegalArgumentException("Street, postal code, and city cannot be null");
    }

    Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
    existingAddress.setStreet(addressDTO.getStreet());
    existingAddress.setPostalCode(addressDTO.getPostalCode());
    existingAddress.setCity(addressDTO.getCity());
    existingAddress.setLatitude(addressDTO.getLatitude());
    existingAddress.setLongitude(addressDTO.getLongitude());

    Address updatedAddress = addressRepository.save(existingAddress);
    return mapToResponseDTO(updatedAddress);
  }

  /**
   * Deletes an address by its ID.
   * @param id the ID of the address to delete.
   * @throws RuntimeException if the address to delete is not found.
   */
  public void deleteById(int id) {
    if (!existsById(id)) {
      throw new RuntimeException("Address not found with id: " + id);
    }
    addressRepository.deleteById(id);
  }

  /**
   * Private method that checks if an address exists by its ID.
   * @param id the ID of the address to check.
   * @return true if the address exists, false otherwise.
   */
  private boolean existsById(int id) {
    return addressRepository.existsById(id);
  }

  /**
   * Retrieves all addresses from the database.
   * @return a list of all addresses.
   */
  public List<AddressResponseDTO> findAllAddresses() {
    List<Address> addresses = addressRepository.findAll();
    return addresses.stream()
        .map(this::mapToResponseDTO)
        .toList();
  }

  
}

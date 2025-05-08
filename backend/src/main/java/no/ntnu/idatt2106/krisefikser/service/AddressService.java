package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.AddressMapper;
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
  private final AddressMapper addressMapper;

  /**
   * Retrieves an address by its ID.
   * @param id the ID of the address to retrieve.
   * @return an Optional containing the address if found, or an empty Optional if not found.
   */
  public Optional<Address> findById(int id) {
    return addressRepository.findById(id);
  }

  /**
   * Saves a new address to the database.
   * @param addressDTO the address to save.
   * @return the saved address.
   * @throws Exception if the address is invalid or cannot be saved.
   */
  public AddressResponseDTO save(AddressRequestDTO addressDTO) throws Exception {
    if (addressDTO.getStreet() == null || addressDTO.getPostalCode() == null || addressDTO.getCity() == null) {
      throw new IllegalArgumentException("Street, postal code, and/or city cannot be null");
    }
    Address address = addressMapper.toEntity(addressDTO);

    return addressMapper.toResponseDTO(addressRepository.save(address));
  }

  /**
   * Updates an existing address in the database.
   * @param id the ID of the address to update.
   * @param addressDTO the new address data.
   * @return the updated address.
   * @throws Exception if the address is not found or fields are invalid
   */
  public AddressResponseDTO updateAddress(int id, AddressRequestDTO addressDTO) throws Exception {
    Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

    if (addressDTO.getStreet() == null || addressDTO.getPostalCode() == null || addressDTO.getCity() == null) {
      throw new IllegalArgumentException("Street, postal code, and city cannot be null");
    }

    existingAddress.setStreet(addressDTO.getStreet());
    existingAddress.setPostalCode(addressDTO.getPostalCode());
    existingAddress.setCity(addressDTO.getCity());
    existingAddress.setLatitude(addressDTO.getLatitude());
    existingAddress.setLongitude(addressDTO.getLongitude());

    Address updatedAddress = addressRepository.save(existingAddress);
    return addressMapper.toResponseDTO(updatedAddress);
  }

  /**
   * Deletes an address by its ID.
   * @param id the ID of the address to delete.
   * @throws RuntimeException if the address to delete is not found.
   */
  public void deleteById(int id) throws RuntimeException {
    if (!addressRepository.existsById(id)) {
      throw new RuntimeException("Address not found with id: " + id);
    }
    addressRepository.deleteById(id);
  }

  /**
   * Retrieves all addresses from the database.
   * @return a list of all addresses.
   */
  public List<AddressResponseDTO> findAllAddresses() {
    List<Address> addresses = addressRepository.findAll();
    return addresses.stream()
        .map(addressMapper::toResponseDTO)
        .toList();
  }  
}

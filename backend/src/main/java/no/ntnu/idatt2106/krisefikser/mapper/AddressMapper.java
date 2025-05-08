package no.ntnu.idatt2106.krisefikser.mapper;

import org.springframework.stereotype.Component;

import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;

@Component
public class AddressMapper {
  
  /**
   * Converts an AddressRequestDTO to an Address entity.
   * @param addressRequestDTO the AddressRequestDTO to convert.
   * @return the converted Address entity.
   */
  public Address toEntity(AddressRequestDTO addressRequestDTO) {
    if (addressRequestDTO == null) {
      return null;
    }
    Address address = new Address();
    address.setStreet(addressRequestDTO.getStreet());
    address.setPostalCode(addressRequestDTO.getPostalCode());
    address.setCity(addressRequestDTO.getCity());
    address.setLatitude(addressRequestDTO.getLatitude());
    address.setLongitude(addressRequestDTO.getLongitude());
    return address;
  }

  /**
   * Converts an Address entity to an AddressResponseDTO.
   * @param address the Address entity to convert.
   * @return the converted AddressResponseDTO.
   */
  public AddressResponseDTO toResponseDTO(Address address) {
    if (address == null) {
      return null;
    }
    AddressResponseDTO dto = new AddressResponseDTO();
    dto.setId(address.getId());
    dto.setStreet(address.getStreet());
    dto.setPostalCode(address.getPostalCode());
    dto.setCity(address.getCity());
    dto.setLatitude(address.getLatitude());
    dto.setLongitude(address.getLongitude());
    return dto;
  }
}

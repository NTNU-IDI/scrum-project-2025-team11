package no.ntnu.idatt2106.krisefikser.mapper;

import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;

/**
 * Mapper class to map household entities to HouseholdResponseDTO
 */
public class HouseholdMapper {

    public static HouseholdResponseDTO toResponseDTO(Household household) {
        HouseholdResponseDTO dto = new HouseholdResponseDTO();
        dto.setId(household.getId());
        dto.setName(household.getName());
        dto.setMemberCount(household.getMemberCount());

        Address address = household.getAddress();
        if (address != null) {
            AddressResponseDTO addressDTO = new AddressResponseDTO();
            addressDTO.setId(address.getId());
            addressDTO.setCity(address.getCity());
            addressDTO.setStreet(address.getStreet());
            addressDTO.setPostalCode(address.getPostalCode());
            addressDTO.setLatitude(address.getLatitude());
            addressDTO.setLongitude(address.getLongitude());

            dto.setAddress(addressDTO);
        }
        return dto;
    }
}

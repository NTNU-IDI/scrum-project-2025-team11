package no.ntnu.idatt2106.krisefikser.mapper;

import java.util.List;

import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;

/**
 * Utility class for mapping Household entities to their corresponding response DTOs.
 * <p>
 * Converts a Household and its associated users into a rich DTO including
 * member details and address information.
 */
public class HouseholdMapper {

    /**
     * Maps a Household entity and its members to a HouseholdResponseDTO.
     *
     * @param household the Household entity to convert
     * @param members list of User entities belonging to the household
     * @return a populated HouseholdResponseDTO containing household info, members, and address
     */
    public static HouseholdResponseDTO toResponseDTO(Household household, List<User> members) {
        HouseholdResponseDTO dto = new HouseholdResponseDTO();
        dto.setId(household.getId());
        dto.setName(household.getName());
        dto.setMemberCount(members.size());

        dto.setMembers(members.stream()
            .map(member -> new HouseholdResponseDTO.HouseholdUserDTO(
                member.getUsername(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmail()))
            .toList());

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

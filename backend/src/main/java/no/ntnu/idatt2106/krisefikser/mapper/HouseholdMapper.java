package no.ntnu.idatt2106.krisefikser.mapper;

import java.util.List;

import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;

/**
 * Mapper class to map household entities to HouseholdResponseDTO
 */
public class HouseholdMapper {

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

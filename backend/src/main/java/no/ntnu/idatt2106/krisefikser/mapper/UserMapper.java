package no.ntnu.idatt2106.krisefikser.mapper;

import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;

public class UserMapper {

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole().toString());
        dto.setHouseholdId(user.getHousehold().getId());
        dto.setHouseholdName(user.getHousehold().getName());

        Household household = user.getHousehold();
        HouseholdResponseDTO householdDTO = new HouseholdResponseDTO();
        householdDTO.setId(household.getId());
        householdDTO.setName(household.getName());
        householdDTO.setMemberCount(household.getMemberCount());

        Address address = household.getAddress();
        if (address != null) {
            AddressResponseDTO addressDTO = new AddressResponseDTO();
            addressDTO.setId(address.getId());
            addressDTO.setCity(address.getCity());
            addressDTO.setStreet(address.getStreet());
            addressDTO.setPostalCode(address.getPostalCode());
            addressDTO.setLatitude(address.getLatitude());
            addressDTO.setLongitude(address.getLongitude());

            householdDTO.setAddress(addressDTO);
        }
        return dto;
    }
    
}

package no.ntnu.idatt2106.krisefikser.service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdRepository;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdUpdateDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing households.
 * This class provides methods for updating, saving, and checking the existence of households.
 */
@Service
@RequiredArgsConstructor
public class HouseholdService {

    /**
     * Repository for performing CRUD operations on Household entities.
     */
    private final HouseholdRepository householdRepository;
    private final AddressService addressService;

    private HouseholdResponseDTO mapToResponseDTO(Household household) {
        HouseholdResponseDTO dto = new HouseholdResponseDTO();
        dto.setId(household.getId());
        dto.setName(household.getName());
        dto.setMemberCount(household.getMemberCount());
        Address address = household.getAddress();
        if (address != null) {
            AddressResponseDTO addressDto = new AddressResponseDTO();
            addressDto.setId(address.getId());
            addressDto.setStreet(address.getStreet());
            addressDto.setPostalCode(address.getPostalCode());
            addressDto.setCity(address.getCity());
            addressDto.setLatitude(address.getLatitude());
            addressDto.setLongitude(address.getLongitude());
            dto.setAddress(addressDto);
        }
        return dto;
    }

    public Optional<Household> findById(int id) {
        return householdRepository.findById(id);
    }

    /**
     * Updates an existing household with new data.
     *
     * @param id        The ID of the household to update.
     * @param household The new household data to update with.
     * @return The updated household entity.
     * @throws RuntimeException if the household with the given ID is not found.
     */
    public HouseholdResponseDTO updateHousehold(int id, HouseholdUpdateDTO newHousehold) {
        Household currentHousehold = householdRepository.findById(id).orElseThrow(() -> new RuntimeException("Household id not found"));
        if (newHousehold.getMemberCount() != 0) {
            currentHousehold.setMemberCount(newHousehold.getMemberCount());
        }
        if (newHousehold.getName() != null) {
            currentHousehold.setName(newHousehold.getName());
        }
        Household updatedHousehold = householdRepository.save(currentHousehold);
        return mapToResponseDTO(updatedHousehold);
    }

    /**
     * Saves a new household to the database.
     *
     * @param newHousehold The household entity to save.
     * @return The saved household entity.
     */
    public HouseholdResponseDTO save(HouseholdRequestDTO newHousehold) throws Exception {
        Household household = new Household();
        if (newHousehold.getName() == null) {
            throw new IllegalArgumentException("Name is missing");
        }
        if (newHousehold.getMemberCount() == 0) {
            throw new IllegalArgumentException("Member count is missing");
        }
        if (newHousehold.getAddress() == null) {
            throw new IllegalArgumentException("Invalid address format, make sure to fill all fields");
        }
        AddressResponseDTO addressResponseDTO = addressService.save(newHousehold.getAddress());
        household.setName(newHousehold.getName());
        household.setMemberCount(newHousehold.getMemberCount());
        household.setAddress(addressService.findById(addressResponseDTO.getId()).orElseThrow(() -> new RuntimeException("Address id not found")));
        Household savedHousehold = householdRepository.save(household);
        return mapToResponseDTO(savedHousehold);
    }

    /**
     * Checks if a household exists by its ID.
     *
     * @param id The ID of the household to check.
     * @return True if the household exists, false otherwise.
     */
    public boolean existsById(int id) {
        return householdRepository.existsById(id);
    } 
    
}

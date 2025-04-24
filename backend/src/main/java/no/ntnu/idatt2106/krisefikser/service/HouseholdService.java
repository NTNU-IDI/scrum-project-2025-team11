package no.ntnu.idatt2106.krisefikser.service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdRepository;
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
    public Household updateHousehold(int id, Household household) {
        Household currentHousehold = householdRepository.findById(id).orElseThrow(() -> new RuntimeException("Household id not found"));
        if (household.getMemberCount() != null) {
            currentHousehold.setMemberCount(household.getMemberCount());
        }
        if (household.getName() != null) {
            currentHousehold.setName(household.getName());
        }
        return householdRepository.save(currentHousehold);
    }

    /**
     * Saves a new household to the database.
     *
     * @param newHousehold The household entity to save.
     * @return The saved household entity.
     */
    public Household save(Household newHousehold) throws Exception {
        // TODO make checks to see if household is valid
        if (newHousehold.getMemberCount() != null || newHousehold.getName() != null || newHousehold.getAddress() != null) {
            throw new IllegalArgumentException("Household has to contain member count, name of household, and an address");
        }
        return householdRepository.save(newHousehold);
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

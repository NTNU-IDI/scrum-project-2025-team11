package no.ntnu.idatt2106.krisefikser.service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdRepository;
import no.ntnu.idatt2106.krisefikser.model.Household;

@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepository;


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

    public Household save(Household newHousehold) {
        // TODO make checks to see if household is valid
        return householdRepository.save(newHousehold);
    }

    public boolean existsById(int id) {
        return householdRepository.existsById(id);
    } 
    
}

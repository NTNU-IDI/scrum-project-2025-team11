package no.ntnu.idatt2106.krisefikser.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.service.HouseholdService;

@RestController
@RequestMapping("/api/household")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Household", description = "Operations related to household management")
public class HouseholdController {

    /**
     * Service for handling household-related business logic.
     */
    private final HouseholdService householdService;

    /**
     * Updates an existing household with new data.
     *
     * @param id        The ID of the household to update.
     * @param household The new household data to update with.
     * @return A {@link ResponseEntity} containing the updated household entity if successful,
     *         or a 404 Not Found response if the household with the given ID does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Household> updateHousehold(@PathVariable int id, @RequestBody Household household) {
        if (!householdService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Household updatedHouseholdCount = householdService.updateHousehold(id, household);
        return ResponseEntity.ok(updatedHouseholdCount);
    }
    
}

package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Update household information based on ID",
            description = "Updates already existing household by finding the given ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Household information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID, ID does not exist")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Household> updateHousehold(@PathVariable int id, @RequestBody Household household) {
        if (!householdService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Household updatedHousehold = householdService.updateHousehold(id, household);
        return ResponseEntity.ok(updatedHousehold);
    }

    @Operation(
            summary = "Create new household",
            description = "Creates a new instance of household by giving name, amount of people in household" +
                          "and address in the form of a Household"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Household successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid household information")
    })
    @PutMapping("/")
    public ResponseEntity<Household> createHousehold(@RequestBody Household household) throws Exception {
        Household newHouseholdEntry = householdService.save(household);
        return ResponseEntity.ok(newHouseholdEntry);
    }
}

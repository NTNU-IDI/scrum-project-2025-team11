package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import no.ntnu.idatt2106.krisefikser.mapper.HouseholdMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdUpdateDTO;
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
    public ResponseEntity<HouseholdResponseDTO> updateHousehold(@PathVariable int id, @RequestBody HouseholdUpdateDTO household) {
        if (!householdService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        HouseholdResponseDTO updatedHousehold = householdService.updateHousehold(id, household);
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
    @PostMapping
    public ResponseEntity<HouseholdResponseDTO> createHousehold(@RequestBody HouseholdRequestDTO household) throws Exception {
        System.out.println("Recieved Household: " + household);
        System.out.println("Raw JSON payload: " + household);
        HouseholdResponseDTO createdHousehold = householdService.save(household);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHousehold);
    }

    @Operation(
            summary = "Get household information",
            description = "Receive household information based on the given id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Household information fetched"),
            @ApiResponse(responseCode = "400", description = "Household could not be found, make sure that the id is correct")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HouseholdResponseDTO> getHousehold(@PathVariable int id) {
        Household household = householdService.findById(id).orElse(null);
        if (household == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(HouseholdMapper.toResponseDTO(household));
        }
    }

    /* TODO add maybe on delete cascade so that it is possible to delete entries
    @Operation(
            summary = "Delete household",
            description = "Delete a entry in the household table based on the id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Household successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Household could not be found, make sure that the id is correct")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousehold(@PathVariable int id) {
        if (!householdService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        householdService.deleteById(id);
        return ResponseEntity.noContent().build();
    }*/

    @Operation(
            summary = "List all households",
            description = "List all the households that is in the household table"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Households fetched"),
            @ApiResponse(responseCode = "400", description = "Households could not be found")
    })
    @GetMapping
    public ResponseEntity<List<HouseholdResponseDTO>> getAllHouseholds() {
        List<HouseholdResponseDTO> householdList = householdService.findAll();
        if (householdList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(householdList);
    }
}

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

    private final HouseholdService householdService;

    @PutMapping("/{id}")
    public ResponseEntity<Household> updateHousehold(@PathVariable int id, @RequestBody Household household) {
        if (!householdService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Household updatedHouseholdCount = householdService.updateHousehold(id, household);
        return ResponseEntity.ok(updatedHouseholdCount);
    }
    
}

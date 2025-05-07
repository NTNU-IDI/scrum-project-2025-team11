package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import no.ntnu.idatt2106.krisefikser.mapper.HouseholdMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdUpdateDTO;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.HouseholdInviteCodeService;
import no.ntnu.idatt2106.krisefikser.service.HouseholdService;
import no.ntnu.idatt2106.krisefikser.service.UserService;


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
    private final UserService userService;
    private final HouseholdInviteCodeService householdInviteCodeService;

    /**
     * Updates an existing household with new data.
     *
     * @param id        The ID of the household to update.
     * @param household The new household data to update with.
     * @return A {@link ResponseEntity} containing the updated household entity if successful,
     *         or a 404 Not Found response if the household with the given ID does not exist.
     */
    @Operation(
            summary = "Update household information based on logged in users information",
            description = "Updates already existing household by using the cookie sent"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Household information updated successfully"),
            @ApiResponse(responseCode = "400", description = "User could not be found")
    })
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public ResponseEntity<HouseholdResponseDTO> updateHousehold(@RequestBody HouseholdUpdateDTO household) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        int hhId = user.getHouseholdId();
        if (!householdService.existsById(hhId)) {
            return ResponseEntity.notFound().build();
        }
        HouseholdResponseDTO updatedHousehold = householdService.updateHousehold(hhId, household);
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
            description = "Receive household information based on the logged in user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Household information fetched"),
            @ApiResponse(responseCode = "400", description = "Household could not be found, make sure that the id is correct")
    })
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<HouseholdResponseDTO> getHousehold() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        int hhId = user.getHouseholdId();
        Household household = householdService.findById(hhId).orElse(null);
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
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public ResponseEntity<List<HouseholdResponseDTO>> getAllHouseholds() {
        List<HouseholdResponseDTO> householdList = householdService.findAll();
        if (householdList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(householdList);
    }


    @Operation(
            summary = "Invite someone to a household",
            description = "Invite someone to a household by writing their email to send them a code to join the household"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email successfully sent"),
            @ApiResponse(responseCode = "404", description = "Household could not be found, make sure that the user is in a household"),
            @ApiResponse(responseCode = "400", description = "Email could not be sent, make sure that the email is valid")
    })
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/invite")
    public ResponseEntity<Void> inviteToHousehold(@RequestParam String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        int hhId = user.getHouseholdId();
        Household household = householdService.findById(hhId).orElse(null);
        if (household == null) {
            return ResponseEntity.notFound().build();
        }
        householdInviteCodeService.initiateCode(email, hhId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Find household by invite code",
            description = "Find household by invite code"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Household fetched"),
            @ApiResponse(responseCode = "400", description = "Household could not be found, make sure that the invite code is correct")
    })
    @GetMapping("/inviteCode")
    public ResponseEntity<HouseholdResponseDTO> getHouseholdByInviteCode(@RequestParam String inviteCode) {
        Household household = householdInviteCodeService.consumeInviteCode(inviteCode);
        if (household == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(HouseholdMapper.toResponseDTO(household));
    }
}

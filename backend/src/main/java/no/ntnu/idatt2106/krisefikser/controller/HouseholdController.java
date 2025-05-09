package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdUpdateDTO;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.HouseholdInviteCodeService;
import no.ntnu.idatt2106.krisefikser.service.HouseholdService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

/**
 * REST controller for managing household operations.
 * <p>
 * Provides endpoints to create, update, retrieve, list, and invite users to households.
 */
@RestController
@RequestMapping("/api/household")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Household", description = "Operations related to household management")
public class HouseholdController {

    private final HouseholdService householdService;
    private final UserService userService;
    private final HouseholdInviteCodeService householdInviteCodeService;

    /**
     * Updates the household belonging to the authenticated user.
     *
     * @param household DTO containing the fields to update
     * @return 200 OK with the updated household data, or 404 Not Found if the household does not exist
     */
    @Operation(
        summary = "Update current user's household",
        description = "Updates the household data for the authenticated user based on the provided update DTO."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Household updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid update data"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "Household not found")
    })
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public ResponseEntity<HouseholdResponseDTO> updateHousehold(@RequestBody HouseholdUpdateDTO household) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int hhId = user.getHouseholdId();
        if (!householdService.existsById(hhId)) {
            return ResponseEntity.notFound().build();
        }
        HouseholdResponseDTO updated = householdService.updateHousehold(hhId, household);
        return ResponseEntity.ok(updated);
    }

    /**
     * Creates a new household.
     *
     * @param household DTO with the required household data
     * @return 201 Created with the created household data, or 400 Bad Request for invalid input
     * @throws Exception if creation fails
     */
    @Operation(
        summary = "Create a new household",
        description = "Creates a new household with given name, size, and address details."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Household created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid household information"),
        @ApiResponse(responseCode = "500", description = "Server error during creation")
    })
    @PostMapping
    public ResponseEntity<HouseholdResponseDTO> createHousehold(@RequestBody HouseholdRequestDTO household) throws Exception {
        HouseholdResponseDTO created = householdService.save(household);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves the household of the authenticated user.
     *
     * @return 200 OK with the household data, or 401 Unauthorized if user not found
     */
    @Operation(
        summary = "Get current user's household",
        description = "Fetches the household details, including members, for the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Household fetched successfully"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "Household not found")
    })
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<HouseholdResponseDTO> getHousehold() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        HouseholdResponseDTO dto = householdService.getHouseholdWithMembers(user.getHouseholdId());
        return ResponseEntity.ok(dto);
    }

    /**
     * Lists all households in the system.
     *
     * @return 200 OK with list of households, or 404 Not Found if none exist
     */
    @Operation(
        summary = "List all households",
        description = "Retrieves a list of all households stored in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Households fetched successfully"),
        @ApiResponse(responseCode = "404", description = "No households found"),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public ResponseEntity<List<HouseholdResponseDTO>> getAllHouseholds() {
        List<HouseholdResponseDTO> list = householdService.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * Sends an invitation code to join the current user's household.
     *
     * @param email Recipient email address
     * @return 201 Created if email sent, 400 Bad Request for invalid email, 404 Not Found if user or household not found
     */
    @Operation(
        summary = "Invite user to household",
        description = "Generates and sends an invite code to the specified email to join the authenticated user's household."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invitation email sent"),
        @ApiResponse(responseCode = "400", description = "Invalid email address"),
        @ApiResponse(responseCode = "401", description = "Authentication required"),
        @ApiResponse(responseCode = "404", description = "User or household not found")
    })
    @SecurityRequirement(name = "jwtCookieAuth")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/invite")
    public ResponseEntity<Void> inviteToHousehold(@RequestParam String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Household hh = householdService.findById(user.getHouseholdId()).orElse(null);
        if (hh == null) {
            return ResponseEntity.notFound().build();
        }
        householdInviteCodeService.initiateCode(email, hh.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves a household using an invite code.
     *
     * @param inviteCode The invitation code
     * @return 200 OK with household data if code valid, 404 Not Found if invalid
     */
    @Operation(
        summary = "Get household by invite code",
        description = "Fetches the household details associated with the provided invite code."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Household fetched successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid invite code")
    })
    @GetMapping("/inviteCode")
    public ResponseEntity<HouseholdResponseDTO> getHouseholdByInviteCode(@RequestParam String inviteCode) {
        HouseholdResponseDTO dto = householdInviteCodeService.consumeInviteCode(inviteCode);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}

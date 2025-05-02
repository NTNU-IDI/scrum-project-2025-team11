package no.ntnu.idatt2106.krisefikser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorConfirmDTO;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorRequestDTO;
import no.ntnu.idatt2106.krisefikser.service.TwoFactorCodeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TwoFactorController {

    private final TwoFactorCodeService twoFactorCodeService;

    @Operation(
            summary = "Request a two-factor authentication code",
            description = "Initiate the two-factor authentication process by sending a code to the user's email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Two-factor authentication code sent successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid email address.")
    })
    @PostMapping("/request-two-factor-code")
    public ResponseEntity<Void> requestTwoFactorCode(@RequestBody @Valid TwoFactorRequestDTO request) {
        twoFactorCodeService.initiateCode(request.getEmail());
        // Always return 200 to avoid user enumeration
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Confirm two-factor authentication",
            description = "Confirm the two-factor authentication process using the provided code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Two-factor authentication confirmed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid or expired code.")
    })
    @PostMapping("/confirm-authentication")
    public ResponseEntity<Void> confirmAuthentication(@RequestBody @Valid TwoFactorConfirmDTO request) {
        twoFactorCodeService.completeAuthentication(request.getCode());
        return ResponseEntity.ok().build();
    }
}

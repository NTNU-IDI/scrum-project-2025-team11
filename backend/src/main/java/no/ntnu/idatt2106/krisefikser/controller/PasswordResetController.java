package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetConfirm;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetRequest;
import no.ntnu.idatt2106.krisefikser.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling password reset workflows.
 * <p>
 * Provides endpoints to request a password reset token and to confirm a new password using a valid token.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Password Reset API", description = "Operations for initiating and completing password resets")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * Initiates the password reset process for a user by email.
     * <p>
     * Sends a password reset token to the provided email address if it exists in the system.
     * Always returns 200 OK to prevent user enumeration.
     *
     * @param request DTO containing the user's email address
     * @return 200 OK indicating the request was received
     */
    @PostMapping("/request-password-reset")
    @Operation(summary = "Request password reset token",
               description = "Generates and emails a password reset token to the user. Returns 200 even if email is unregistered.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset request accepted"),
        @ApiResponse(responseCode = "400", description = "Invalid email format provided")
    })
    public ResponseEntity<Void> requestPasswordReset(
        @Valid @RequestBody PasswordResetRequest request
    ) {
        passwordResetService.initiateReset(request.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * Completes the password reset by validating the token and setting a new password.
     *
     * @param request DTO containing the reset token and new password
     * @return 200 OK if token was valid and password updated, 400 Bad Request for invalid token or password
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Confirm password reset",
               description = "Validates the reset token and updates the user's password.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid token or password criteria not met")
    })
    public ResponseEntity<Void> resetPassword(
        @Valid @RequestBody PasswordResetConfirm request
    ) {
        passwordResetService.completeReset(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

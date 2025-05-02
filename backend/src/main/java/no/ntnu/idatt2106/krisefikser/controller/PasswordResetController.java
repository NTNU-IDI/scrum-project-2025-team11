package no.ntnu.idatt2106.krisefikser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetRequest;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetConfirm;
import no.ntnu.idatt2106.krisefikser.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Controller for handling password reset requests.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Password Reset", description = "Operations related to password reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * Initiates a password reset process by sending a reset token to the user's email.
     *
     * @param request the password reset request containing the user's email
     * @return a response entity indicating the result of the operation
     */
    @PostMapping("/request-password-reset")
    @Tag(name = "Password Reset", description = "Request a password reset")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody @Valid PasswordResetRequest request) {
        passwordResetService.initiateReset(request.getEmail());
        // Always return 200 to avoid user enumeration
        return ResponseEntity.ok().build();
    }

    /**
     * Completes the password reset process by validating the token and updating the user's password.
     *
     * @param request the password reset confirmation containing the token and new password
     * @return a response entity indicating the result of the operation
     */
    @PostMapping("/reset-password")
    @Tag(name = "Password Reset", description = "Reset a password with a token")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetConfirm request) {
        passwordResetService.completeReset(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

package no.ntnu.idatt2106.krisefikser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetRequest;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetConfirm;
import no.ntnu.idatt2106.krisefikser.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request-password-reset")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody @Valid PasswordResetRequest request) {
        passwordResetService.initiateReset(request.getEmail());
        // Always return 200 to avoid user enumeration
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetConfirm request) {
        passwordResetService.completeReset(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

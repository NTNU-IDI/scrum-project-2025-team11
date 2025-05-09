package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for confirming two-factor authentication.
 */
@Data
public class ConfirmAuthenticationRequest {
    @Valid
    @NotNull(message = "Two-factor code is required")
    private TwoFactorConfirmDTO twoFactorCode;

    @Valid
    private LoginRequest login;

}
package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for confirming a password reset.
 */

@Getter
@Setter
@AllArgsConstructor
public class PasswordResetConfirm {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}

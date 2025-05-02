package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * DTO for requesting a password reset.
 */
@Getter
@Setter
@AllArgsConstructor
public class PasswordResetRequest {
    @NotBlank
    @Email
    private String email;
}

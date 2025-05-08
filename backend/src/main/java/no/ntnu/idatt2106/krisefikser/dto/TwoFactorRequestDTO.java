package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for requesting a two-factor authentication code.
 */
@Getter @Setter
@AllArgsConstructor
public class TwoFactorRequestDTO {
    @NotBlank
    private String username;
    
}

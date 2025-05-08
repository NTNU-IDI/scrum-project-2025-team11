package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for confirming a two-factor authentication code.
 */
@AllArgsConstructor
@Getter @Setter
public class TwoFactorConfirmDTO {
    @NotBlank
    private String code;
    
}

package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TwoFactorRequestDTO {
    @NotBlank
    @Email
    private String email;
    
}

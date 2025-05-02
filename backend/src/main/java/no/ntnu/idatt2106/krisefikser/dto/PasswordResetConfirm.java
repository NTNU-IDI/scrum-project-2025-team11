package no.ntnu.idatt2106.krisefikser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirm {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}

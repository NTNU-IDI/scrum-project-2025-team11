package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO for user login requests.
 */
@Data
public class LoginRequest {
    @Schema(description = "Username given by the user", example = "SolbakkenJon")
    private String username;
    @Schema(description = "Password entered by the user", example = "passordhash3")
    private String password;
}

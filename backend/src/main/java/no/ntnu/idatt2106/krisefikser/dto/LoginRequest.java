package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(description = "Username given by the user", example = "thelegend27")
    private String username;
    @Schema(description = "Password entered by the user", example = "passordhash1")
    private String password;
}

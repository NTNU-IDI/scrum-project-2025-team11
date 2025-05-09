package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for returning user data.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data returned for a User")
public class UserResponseDTO {

    @Schema(description = "Unique identifier for the user.", example = "1")
    private int id;

    @Schema(description = "Email address of the user.", example = "dontru@gmail.com")
    private String email;

    @Schema(description = "Username of the user.", example = "maga123")
    private String username;

    @Schema(description = "First name of the user.", example = "Donald")
    private String firstName;

    @Schema(description = "Last name of the user.", example = "Trump")
    private String lastName;

    @Schema(description = "Role of the user.", example = "normal")
    private String role;
    
    @Schema(description = "Household ID of the user.", example = "1")
    private int householdId;

    @Schema(description = "Household name of the user.", example = "Household 1")
    private String householdName;

}

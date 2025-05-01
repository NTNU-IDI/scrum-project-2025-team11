package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(description = "Payload for creating or updating an user")
public class UserRequestDTO {

    @Schema(description = "Email address of the user.", example ="trump@mail.com")
    private String email;

    @Schema(description = "Username of the user.", example = "maga123")
    private String username;

    @Schema(description = "First name of the user.", example = "Donald")
    private String firstName;

    @Schema(description = "Last name of the user.", example = "Trump")
    private String lastName;

    @Schema(description = "Password of the user.", example = "P@ssw0rd")
    private String password;

    @Schema(description = "Household ID of the user.", example = "1")
    private int householdId;

}

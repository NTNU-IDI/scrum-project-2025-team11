package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for updating user information.
 * This class is used to transfer data between the client and server
 * when updating user information.
 */
@Getter @Setter @NoArgsConstructor
public class UserUpdateDTO  {          // only fields that may change

    private String username;
    private String email;
    private String firstName;
    private String lastName;
}


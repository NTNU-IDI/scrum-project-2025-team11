package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserUpdateDTO  {          // only fields that may change

    private String username;
    private String email;
    private String firstName;
    private String lastName;
}


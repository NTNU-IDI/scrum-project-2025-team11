package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Data;

@Data
public class UserPatchDTO  {          // only fields that may change
    private String email;
    private String firstName;
    private String lastName;
    private Integer householdId;
}


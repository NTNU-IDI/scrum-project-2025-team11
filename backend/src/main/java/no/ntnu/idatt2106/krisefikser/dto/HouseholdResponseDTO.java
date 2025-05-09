package no.ntnu.idatt2106.krisefikser.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for returning household data to the client.
 */
@Data
public class HouseholdResponseDTO {
    @Schema(description = "Unique identifier in the household table", example = "1")
    private int id;

    @Schema(description = "Name of the household", example = "Garv sin hule")
    private String name;

    @Schema(description = "How many members belong to the given household", example = "4")
    private Integer memberCount;

    @Schema(description = "List of household members")
    private List<HouseholdUserDTO> members;
    
    @Schema(description = "Address of the household")
    private AddressResponseDTO address;  
    
    
    @Data
    @Schema(description = "User entity representing a user in the household.")
    @AllArgsConstructor
    public static class HouseholdUserDTO {
        
        @Schema(description = "Username of the user.", example = "maga123")
        private String username;
        
        @Schema(description = "First name of the user.", example = "Donald")
        private String firstName;
        
        @Schema(description = "Last name of the user.", example = "Trump")
        private String lastName;

        @Schema(description = "Email address of the user.", example = "dontru@gmail.com")
        private String email;
    }
}

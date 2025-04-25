package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseholdRequestDTO {
    private String name;
    private int memberCount;
    private int addressId;
}

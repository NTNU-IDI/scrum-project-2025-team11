package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Data;

@Data
public class HouseholdResponseDTO {
    private int id;
    private String name;
    private Integer memberCount;
    private AddressResponseDTO address; 
}

package no.ntnu.idatt2106.krisefikser.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * Composite key class for HouseholdItem entity.
 * This class is used to represent the composite key consisting of householdId, itemId, and acquiredDate.
 * 
 * It implements Serializable and overrides equals and hashCode methods for proper comparison.
 */
@Embeddable
@Data
@NoArgsConstructor
public class HouseholdItemId implements Serializable {
    private Integer householdId;
    private Integer itemId;
    private LocalDate acquiredDate;

    public HouseholdItemId(Integer householdId,
                           Integer itemId,
                           LocalDate acquiredDate) {
        this.householdId  = householdId;
        this.itemId       = itemId;
        this.acquiredDate = acquiredDate;
    }
}

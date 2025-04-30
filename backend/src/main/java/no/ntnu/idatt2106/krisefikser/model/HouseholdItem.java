package no.ntnu.idatt2106.krisefikser.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an item in a household's inventory.
 * This entity is linked to both the Household and Item entities.
 * It contains information about the quantity, unit, and expiration date of the item.
 */
@Entity
@Table(name = "HOUSEHOLD_ITEMS")
@Getter @Setter @NoArgsConstructor
public class HouseholdItem {


  /**
   * Unique identifier for the household item.
   * This is a composite key consisting of householdId, itemId, and acquiredDate.
   * See HouseholdItemId class.
   */
  @EmbeddedId
  private HouseholdItemId id;

  /**
   * Reference to the household associated with this item.
   * This establishes a many-to-one relationship with the Household entity.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("householdId")
  @JoinColumn(name = "household_id")
  private Household household;

  /**
   * Reference to the item associated with this household item.
   * This establishes a many-to-one relationship with the Item entity.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("itemId")
  @JoinColumn(name = "item_id")
  private Item item;

  /**
   * The date when the item was acquired.
   * This field is part of the composite key and is not directly modifiable.
   */
  @Column(nullable = false, insertable = false, updatable = false)
  public LocalDate getAcquiredDate() {
    return id.getAcquiredDate();
  }

  private BigDecimal quantity;
  private String unit;
  private LocalDate expirationDate;

  public HouseholdItem(HouseholdItemId id,
                       Household household,
                       Item item,
                       BigDecimal quantity,
                       String unit,
                       LocalDate expirationDate) {
    this.id         = id;
    this.household  = household;
    this.item       = item;
    this.quantity   = quantity;
    this.unit       = unit;
    this.expirationDate = expirationDate;
  }
}

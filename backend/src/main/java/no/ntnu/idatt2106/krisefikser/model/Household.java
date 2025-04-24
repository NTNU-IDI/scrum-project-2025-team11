package no.ntnu.idatt2106.krisefikser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a household in the system.
 * This class is used to store information about the household
 * such as id, name of household, how many members there are
 * and the address
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "household")
public class Household {

  /**
   * Unique identifier for the household.
   * This is the primary key and is auto-generated.
   */
  @Schema(description = "Unique identifier for household", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * Name of the household.
   * This field is mandatory and has a maximum length of 255 characters.
   */
  @Schema(description = "Name of the household", example = "Jonas sitt hus")
  @Column(name = "name",nullable = false, length = 255)
  private String name;

  /**
   * Number of members in the household.
   * This field is mandatory and stores the count of people in the household.
   */
  @Schema(description = "Count of how many people exist in a household", example = "4")
  @Column(name = "member_count", nullable = false)
  private Integer memberCount;

  /**
   * Reference to the address associated with the household.
   * This field establishes a many-to-one relationship with the Address entity.
   */
  @Schema(description = "Identifier to connect the column with the Address table", example = "1")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;
}
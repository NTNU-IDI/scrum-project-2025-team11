package no.ntnu.idatt2106.krisefikser.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing an address in the system.
 * This class is used to store address information such as street, postal code, city, latitude, and longitude.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "address")
public class Address {
  
  /**
   * Unique identifier for the address.
   * This field is automatically generated in the database and should not be manually set.
   */
  @Schema(description = "Unique identifier for the address.", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The street address of the location.
   * This field is required and cannot be null.
   */
  @Schema(description = "The street address of the location.", example = "Heiabakken 123")
  @Column(name = "street", nullable = false)
  private String street;

  /**
   * The postal code of the location.
   * This field is required and cannot be null.
   */
  @Schema(description = "The postal code of the location.", example = "1234")
  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  /**
   * The city of the location.
   * This field is required and cannot be null.
   */
  @Schema(description = "The city of the location.", example = "Trondheim")
  @Column(name = "city", nullable = false)
  private String city;

  /**
   * The latitude of the location.
   * This field is required and cannot be null.
   */
  @Schema(description = "The latitude of the location.", example = "73.4305")
  @Column(name = "latitude", nullable = false)
  private Double latitude;

  /**
   * The longitude of the location.
   * This field is required and cannot be null.
   */
  @Schema(description = "The longitude of the location.", example = "18.5555")
  @Column(name = "longitude", nullable = false)
  private Double longitude;
}

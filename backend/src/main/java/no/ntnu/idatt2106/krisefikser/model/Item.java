package no.ntnu.idatt2106.krisefikser.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing an Item in the database.
 * This class is used to map the ITEM table in the database.
 * 
 * It contains the following fields:
 * - id: Unique identifier for the item
 * - name: Name of the item
 * - description: Detailed description of the item
 */

@Entity
@Table (name = "ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Stockable item")
public class Item {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for item", example = "1")
    private Integer id;

    @Column(nullable = false, length = 255)
    @Schema(description = "Name of the item", example = "Hammer")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Description of the item", example = "A tool for hammering nails")
    private String description;
}

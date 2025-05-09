package no.ntnu.idatt2106.krisefikser.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

/**
 * Entity representing an invite code for joining a household.
 * <p>
 * Stores a unique code, its expiry datetime, and a reference to the associated household.
 */
@Entity
@Table(name = "household_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HouseholdInviteCode {

    /**
     * Primary key: Unique identifier for the household invite code.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the household code.")
    private int id;

    /**
     * The unique code used for inviting users to the household.
     */
    @Column(nullable = false, unique = true)
    @Schema(description = "The actual household code.", example = "123456")
    private String code;

    /**
     * The date and time when this invite code will no longer be valid.
     */
    @Column(name = "expiry_date", nullable = false)
    @Schema(description = "The date and time when the household code expires.", example = "2023-10-01T12:00:00")
    private LocalDateTime expiryDate;

    /**
     * Reference to the household that this code grants access to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    /**
     * Checks if the invite code has expired compared to the current date and time.
     *
     * @return true if the current time is after the expiryDate, false otherwise
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}

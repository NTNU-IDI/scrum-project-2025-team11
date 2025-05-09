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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a two-factor authentication code issued to a user.
 * <p>
 * Stores the code value, its expiration timestamp, and the associated user.
 */
@Entity
@Table(name = "two_factor_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TwoFactorCode {

    /**
     * Primary key: Unique identifier for the two-factor code entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the two-factor code.")
    private int id;

    /**
     * The one-time two-factor authentication code.
     */
    @Column(nullable = false, unique = true)
    @Schema(description = "The actual two-factor code.", example = "123456")
    private String code;

    /**
     * The date and time when this two-factor code will expire and become invalid.
     */
    @Column(name = "expiry_date", nullable = false)
    @Schema(description = "The date and time when the two-factor code expires.", example = "2023-10-01T12:00:00")
    private LocalDateTime expiryDate;

    /**
     * The user to whom this two-factor code is issued.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Checks whether the two-factor code has expired compared to the current time.
     *
     * @return true if the current time is after the expiryDate, false otherwise.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
package no.ntnu.idatt2106.krisefikser.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a JWT refresh token assigned to a user.
 * <p>
 * Stores the token string, association to the user, creation and expiration timestamps, and revocation status.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "REFRESH_TOKEN")
public class RefreshToken {

    /**
     * Primary key: Unique identifier for the refresh token entry.
     */
    @Schema(description = "Unique identifier for the point of interest.", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Randomly generated token string used for refreshing JWTs.
     */
    @Schema(description = "Randomly generated string which acts as the token", example = "ehjfdhsifeuuybewnkd")
    private String token;

    /**
     * The user to whom this refresh token belongs. One-to-one relationship.
     */
    @Schema(description = "A link between a entry in the refresh token table to a user in user table", example = "1")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    /**
     * Timestamp indicating when this refresh token was created.
     */
    @Schema(description = "Time stamp showing date and time of when the token was created", example = "2025-05-01T10:15:05")
    private LocalDateTime createdAt;

    /**
     * Timestamp indicating when this refresh token will expire.
     */
    @Schema(description = "Time stamp showing date and time of when the token expires", example = "2025-05-08T10:15:05")
    private LocalDateTime expirationDate;

    /**
     * Flag indicating whether the token has been revoked and is no longer valid.
     */
    @Schema(description = "A boolean letting us know wether the token is invalid", example = "FALSE")
    private Boolean revoked;
}

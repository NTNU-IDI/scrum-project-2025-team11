package no.ntnu.idatt2106.krisefikser.model;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@Table(name = "REFRESH_TOKEN")
public class RefreshToken {

    /**
     * Unique identifier given to each point
     */
    @Schema(description = "Unique identifier for the point of interest.", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Schema(description = "Randomly generated string which acts as the token", example = "ehjfdhsifeuuybewnkd")
    private String token;

    @Schema(description = "A link between a entry in the refresh token table to a user in user table",
            example = "1")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Schema(description = "Time stamp showing date and time of when the token was created",
            example = "2025-05-01T10:15:05")
    private LocalDateTime createdAt;

    @Schema(description = "Time stamp showing date and time of when the token expires",
            example = "2025-05-08T10:15:05")
    private LocalDateTime expirationDate;

    @Schema(description = "A boolean letting us know wether the token is invalid",
            example = "FALSE")
    private Boolean revoked;
    
}

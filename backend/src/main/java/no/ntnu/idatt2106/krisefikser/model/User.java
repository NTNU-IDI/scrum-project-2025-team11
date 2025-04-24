package no.ntnu.idatt2106.krisefikser.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User entity representing a user in the system.")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the user.")
    private Long id;

    /**
     * User's email address.
     */
    @Column(nullable = false)
    @Schema(description = "Email address of the user.")
    private String email;

    /**
     * Username of the User.
     */
    @Column(nullable = false, unique = true)
    @Schema(description = "Username of the user.")
    private String username;

    /**
     * First name of the User.
     * This field cannot be null and has a maximum length of 50 characters.
     */
    @Schema(description = "First name of the user.", example = "Jonny")
    @Column(nullable = false, length = 50)
    private String firstName;

    /**
     * Last name of the User.
     * This field cannot be null and has a maximum length of 50 characters.
     */
    @Schema(description = "Last name of the user.", example = "Doe")
    @Column(nullable = false, length = 50)
    private String lastName;

    /**
     * Password of the User.
     * This field cannot be null and has a maximum length of 255 characters.
     */
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Password of the user.")
    private String password;

    /**
     * Role of the User.
     */
    @Column(nullable = false)
    @Positive
    @Schema(description = "Role of the user.")
    private String role;

    /**
     * Houshold of the User.
     */
    @Schema(description = "Household the user is in", example = "1")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

}

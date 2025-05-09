package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PasswordChangeDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserUpdateDTO;
import no.ntnu.idatt2106.krisefikser.mapper.UserMapper;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management operations.
 * <p>
 * Provides endpoints to fetch, list, update, delete users, and change passwords.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwtCookieAuth")
@PreAuthorize("isAuthenticated()")
@Tag(name = "User", description = "Operations related to user management")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves the authenticated user's profile.
     *
     * @return 200 OK with user data, or 404 Not Found if user does not exist
     */
    @Operation(summary = "Get current user",
    description = "Returns the profile of the currently authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.toResponseDTO(user));
    }

    /**
     * Lists all users in the system.
     *
     * @return 200 OK with list of users, or 204 No Content if none found
     */
    @Operation(summary = "List all users",
    description = "Returns a list of all user profiles in the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users returned successfully",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "204", description = "No users found", content = @Content)
    })
    @PreAuthorize("hasRole('super_admin')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> list() {
        List<UserResponseDTO> users = userService.findAll();
        return users.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(users);
    }

    /**
     * Changes the password for the authenticated user.
     *
     * @param dto payload containing current and new passwords
     * @return 204 No Content on success, or 400 Bad Request if validation fails
     */
    @Operation(summary = "Change password",
    description = "Updates the password of the currently authenticated user after verifying the current password.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid current password or new password criteria not met",
        content = @Content)
    })
    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(
        @Parameter(description = "Password change payload", required = true)
        @Valid @RequestBody PasswordChangeDTO dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())
            || dto.getNewPassword() == null
            || dto.getNewPassword().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        userService.changePassword(user.getId(), dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the authenticated user's profile.
     *
     * @param updateDTO payload with updated user fields
     * @return 200 OK with updated user, or 400/409/404 on errors
     */
    @Operation(summary = "Update user profile",
    description = "Modifies the profile of the currently authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid update data provided", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Email or username already taken", content = @Content)
    })
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(
        @Parameter(description = "Updated user payload", required = true)
        @Valid @RequestBody UserUpdateDTO updateDTO
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existing = userService.getUserByUsername(auth.getName()).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        if (userService.emailExists(updateDTO.getEmail())
            || userService.usernameExists(updateDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        UserResponseDTO updated = userService.updateUser(existing.getId(), updateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a user by ID (admin-only operation).
     *
     * @param id the user identifier
     * @return 204 No Content on success, or 404 Not Found if user does not exist
     */
    @Operation(summary = "Delete user",
    description = "Removes a user identified by ID from the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID of the user to delete", required = true)
        @PathVariable int id
    ) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

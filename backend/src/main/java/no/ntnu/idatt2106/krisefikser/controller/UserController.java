package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PasswordChangeDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserUpdateDTO;
import no.ntnu.idatt2106.krisefikser.mapper.UserMapper;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwtCookieAuth")
@PreAuthorize("isAuthenticated()")
@Tag(name = "User", description = "Operations related to user management")
public class UserController {
  private final UserService userService;
  private final JwtUtil jwtUtil;


  /**
   * Retrieves a user by their unique identifier.
   *
   * @param id the unique identifier of the user
   * @return {@code ResponseEntity} containing the user if found, otherwise
   *         returns a 404 Not Found response
   */
  @Operation(
      summary     = "Get user by ID",
      description = "Retrieves the user details for the specified ID.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User found",
          content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(
      @Parameter(description = "Unique user ID", required = true)
      @PathVariable int id) {
    User user = userService.getUserById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(UserMapper.toResponseDTO(user));
  }


  /**
   * Endpoint to retrieve all users in the system.
   * 
   * @return a list of all users
   */
  @Operation(
      summary     = "List users",
      description = "Returns every user in the system.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Users returned"),
      @ApiResponse(responseCode = "204", description = "No users found",
          content = @Content)
  })
  @PreAuthorize("hasRole('admin')")
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> list() {
    List<UserResponseDTO> users = userService.findAll();
    if (users == null) {
      return ResponseEntity.notFound().build();
    }
    return users.isEmpty()
         ? ResponseEntity.noContent().build()
         : ResponseEntity.ok(users);
  }


  /**
   * Changes the password of a user by their unique identifier.
   *
   * @param id the unique identifier of the user
   * @param dto the password change request
   * @return {@code ResponseEntity} indicating the result of the operation
   */
  @Operation(
      summary     = "Change password",
      description = "Changes the user’s password after verifying current password.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Password changed"),
      @ApiResponse(responseCode = "400", description = "Current password wrong",
          content = @Content)
  })
  @PostMapping("/{id}/password")
  public ResponseEntity<Void> changePassword(
      @Parameter(description = "User ID", required = true)
      @PathVariable int id,
      @Parameter(description = "Password change payload", required = true)
      @RequestBody PasswordChangeDTO dto) {
    User user = userService.getUserById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    if (!user.getPassword().equals(dto.getCurrentPassword())) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST) // Bad Request
          .body(null);
    }
    if (dto.getNewPassword() == null || dto.getNewPassword().isEmpty()) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST) // Bad Request
          .body(null);
    }

    userService.changePassword(id, dto);
    return ResponseEntity.noContent().build();     // 204
  }

  /**
   * Deletes a user by their unique identifier.
   *
   * @param id the unique identifier of the user
   * @return {@code ResponseEntity} indicating the result of the operation
   */
  @Operation(
      summary     = "Delete user",
      description = "Deletes a user (admin only).")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User deleted",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "User ID", required = true)
      @PathVariable int id) {
    
    User user = userService.getUserById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }


  /**
   * Updates an existing user entity.
   *
   * @param user the updated user entity
   * @return {@code ResponseEntity} containing the updated user entity
   */
  @Operation(
    summary = "Update user",
    description = "Updates an existing user entity in the system."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content),
      @ApiResponse(responseCode = "409", description = "Email or username already taken",
          content = @Content),
      @ApiResponse(responseCode = "200", description = "User updated successfully",
          content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
  })
  @PutMapping
  public ResponseEntity<UserResponseDTO> updateUser(
    @Parameter(description = "Updated user object", required = true)
    @RequestBody UserUpdateDTO user) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    User existingUser = userService.getUserByUsername(username).orElse(null);
    if (existingUser == null) {
      return ResponseEntity.notFound().build();
    }
    if (userService.emailExists(user.getEmail())) {
      return ResponseEntity
          .status(409) // Conflict
          .body(null);
    }
    if (userService.usernameExists(user.getUsername())) {
      return ResponseEntity
          .status(409) // Conflict
          .body(null);
    }

    UserResponseDTO updatedUser = userService.updateUser(existingUser.getId(), user);
    return ResponseEntity.ok(updatedUser);
  }  
}

package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "User", description = "Operations related to user management")
public class UserController {
  private final UserService userService;


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
   * @param id the unique identifier of the user to be updated
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
  @PostMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(
    @Parameter(description = "The unique identifier of the user", required = true)
    @PathVariable int id,
    @Parameter(description = "Updated user object", required = true)
    @RequestBody UserUpdateDTO user) {
    User existingUser = userService.getUserById(id).orElse(null);
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

    UserResponseDTO updatedUser = userService.updateUser(id, user);
    return ResponseEntity.ok(updatedUser);
  }  
}

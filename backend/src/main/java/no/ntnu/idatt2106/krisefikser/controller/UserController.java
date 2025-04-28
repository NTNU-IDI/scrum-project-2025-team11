package no.ntnu.idatt2106.krisefikser.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import no.ntnu.idatt2106.krisefikser.dto.UserPatchDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserRequest;
import no.ntnu.idatt2106.krisefikser.dto.UserResponse;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "User", description = "Operations related to user management")
public class UserController {
    private final UserService userService;

  // /**
  //  * Retrieves the currently authenticated user.
  //  *
  //  * @return {@code ResponseEntity} containing the current user's profile
  //  *         information
  //  */
  //   @Operation(
  //     summary     = "Get current user",
  //     description = "Returns the profile of the currently authenticated user.")
  // @ApiResponse(responseCode = "200", description = "Current user",
  //     content = @Content(mediaType = "application/json",
  //               schema = @Schema(implementation = UserResponse.class)))
  // @GetMapping("/me")
  // public ResponseEntity<UserResponse> me() {
  //   return ResponseEntity.ok(userService.getCurrentUser());
  // }

  /**
   * Saves a new user entity.
   *
   * @param user the user entity to be saved
   * @return {@code ResponseEntity} containing the saved user entity
   */
  @Operation(
    summary     = "Register new user",
    description = "Creates a new user. Authentication *not* required.")
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "User created",
        content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))),
    @ApiResponse(responseCode = "409", description = "Email or username already taken",
        content = @Content)
})
@PostMapping
public ResponseEntity<UserResponse> register(
    @Parameter(description = "User registration payload", required = true)
    @RequestBody UserRequest body) {

  UserResponse saved = userService.saveUser(body);
  return ResponseEntity
          .created(URI.create("/api/users/" + saved.getId()))
          .body(saved);                       // 201
}

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
                    schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getById(
      @Parameter(description = "Unique user ID", required = true)
      @PathVariable int id) {

    return ResponseEntity.ok(userService.getUserById(id));
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
  public ResponseEntity<List<UserResponse>> list() {
    List<UserResponse> users = userService.findAll();
    return users.isEmpty()
         ? ResponseEntity.noContent().build()
         : ResponseEntity.ok(users);
  }

  // /**
  //  * Updates a user by their unique identifier.
  //  *
  //  * @param id the unique identifier of the user
  //  * @param patch the fields to update
  //  * @return {@code ResponseEntity} containing the updated user if found,
  //  *         otherwise returns a 404 Not Found response
  //  */
  // @Operation(
  //     summary     = "Update user (partial)",
  //     description = "Updates non-sensitive profile fields (name, e-mail, household).")
  // @ApiResponse(responseCode = "200", description = "User updated",
  //     content = @Content(mediaType = "application/json",
  //               schema = @Schema(implementation = UserResponse.class)))
  // @PatchMapping("/{id}")
  // public ResponseEntity<UserResponse> patch(
  //     @Parameter(description = "User ID", required = true)
  //     @PathVariable int id,
  //     @Parameter(description = "Fields to update") 
  //     @RequestBody UserPatchDTO patch) {

  //   UserResponse updated = userService.patchUser(id, patch);
  //   return ResponseEntity.ok(updated);
  // }

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
  @ApiResponse(responseCode = "204", description = "User deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "User ID", required = true)
      @PathVariable int id) {

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
  @ApiResponse(responseCode = "200", description = "User updated successfully",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
  @PostMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
    @Parameter(description = "The unique identifier of the user", required = true)
    @PathVariable int id,
    @Parameter(description = "Updated user object", required = true)
    @RequestBody UserRequest user) {
    UserResponse updatedUser = userService.updateUser(id, user);
    return ResponseEntity.ok(updatedUser);
  }

  
    
}

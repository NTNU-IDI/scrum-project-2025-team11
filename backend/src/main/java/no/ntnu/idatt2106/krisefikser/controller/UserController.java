package no.ntnu.idatt2106.krisefikser.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

     /**
   * Retrieves the currently logged-in user.
   *
   * @param authentication the authentication object containing user details
   * @return {@code ResponseEntity} containing the logged-in user if found,
   *         otherwise returns a 401 Unauthorized response
   */
  @Operation(
      summary = "Get logged-in user",
      description = "Retrieves the currently logged-in user's details.",
      security = { @SecurityRequirement(name = "bearer-key") }
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User retrieved successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: No valid authentication provided", content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/me")
  public ResponseEntity<User> getLoggedInUser(
    @Parameter(description = "Authentication object containing user details", required = true)
    Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
    }

    String username = authentication.getName();
    User user = userService.getUserByUsername(username);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Not Found
    }
    return ResponseEntity.ok(user); // OK
  }
  
  /**
   * Retrieves a user by their unique identifier.
   *
   * @param id the unique identifier of the user
   * @return {@code ResponseEntity} containing the user if found, otherwise
   *         returns a 404 Not Found response
   */
  @Operation(
    summary = "Get user by ID", 
    description = "Retrieves the user details for the specified user ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found", 
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(
    @Parameter(description = "The unique identifier of the user", required = true)
    @PathVariable Long id) {
    return userService.getUserById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Saves a new user entity.
   *
   * @param user the user entity to be saved
   * @return {@code ResponseEntity} containing the saved user entity
   */
  @Operation(
    summary = "Register a new user",
    description = "Saves a new user entity to the system. No authentication is required to register."
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User saved successfully",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
  })
  @PostMapping
  public ResponseEntity<User> saveUser(
    @Parameter(description = "User object to be created", required = true)
    @RequestBody User user) {
    User newUser = userService.save(user);
    return ResponseEntity.ok(newUser);
  }


  /**
   * Updates an existing user's details.
   *
   * @param id   the unique identifier of the user to be updated
   * @param user the updated user data
   * @return ResponseEntity with updated user information
   */
  @Operation(
    summary = "Update an existing user", 
    description = "Updates the details of an existing user. Requires authentication.", 
    security = { @SecurityRequirement(name = "bearer-key") })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(
    @Parameter(description = "The unique identifier of the user", required = true)
    @PathVariable Long id,
    @Parameter(description = "Updated user data", required = true)
    @RequestBody User user) {
    User updatedUser = userService.updateUser(id, user);
    return ResponseEntity.ok(updatedUser);
  }
    
}

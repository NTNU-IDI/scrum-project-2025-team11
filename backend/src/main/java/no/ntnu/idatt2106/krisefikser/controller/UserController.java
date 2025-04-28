package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
  public ResponseEntity<UserResponse> getUserById(
    @Parameter(description = "The unique identifier of the user", required = true)
    @PathVariable int id) {
    return ResponseEntity.ok(userService.getUserById(id));
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
  public ResponseEntity<UserResponse> saveUser(
    @Parameter(description = "User object to be created", required = true)
    @RequestBody UserRequest user) {
    UserResponse newUser = userService.saveUser(user);
    return ResponseEntity.ok(newUser);
  }


  /**
   * Endpoint to retrieve all users in the system.
   * 
   * @return a list of all users
   */
  @Operation(
    summary = "Get all users", 
    description = "Retrieve a list of all users in the system"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
    @ApiResponse(responseCode = "204", description = "No users found")
  })
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.findAll();
    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(users);
    }
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

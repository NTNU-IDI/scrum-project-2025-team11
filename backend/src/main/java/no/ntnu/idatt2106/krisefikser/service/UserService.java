package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.UserRequest;
import no.ntnu.idatt2106.krisefikser.dto.UserResponse;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.model.User.Role;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HouseholdService householdService;

    /**
     * Finds a user by their ID.
     * @param id
     * @return Optional<User> object containing the user if found, otherwise empty
     */
    public UserResponse getUserById(int id) {
      User user = userRepository.findById(id);
      return mapToResponse(user);
    }

    /**
     * Finds a user by their username.
     * @param username
     * @return User object if found, otherwise null
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by their email.
     * @param email
     * @return User object if found, otherwise null
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserResponse> findAll() {
      return userRepository.findAll().stream()
          .map(this::mapToResponse)
          .toList();

    }

    /**
     * Saves a user to the database.
     * @param user
     * @return User object that was saved
     */
    public UserResponse saveUser(UserRequest user) {
      User newUser = new User();
      newUser.setEmail(user.getEmail());
      newUser.setUsername(user.getUsername());
      newUser.setFirstName(user.getFirstName());
      newUser.setLastName(user.getLastName());
      
      Household household = householdService.findById(user.getHouseholdId()).orElseThrow(() -> new RuntimeException("Household not found"));
      newUser.setHousehold(household);
      newUser.setPassword(user.getPassword()); // Hash the password before saving

      newUser.setRole(Role.normal);
      User savedUser = userRepository.save(newUser);
      return mapToResponse(savedUser);


    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPassword(user.getPassword());
        response.setRole(user.getRole().toString());
        return response;
    }

    /**
     * Deletes a user by their ID.
     * @param id
     */
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    /**
     * Updates a user by their ID.
     * @param id
     * @param updatedUser
     * @return User object that was updated
     */
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
          // Update fields based on the values in the updatedUser object
          if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
          }
          if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
          }
          if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
          }
          if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
          }
          if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword()); // Hash the password before saving
          }
          if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
          }
    
          // Save the updated user entity
          return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    
}

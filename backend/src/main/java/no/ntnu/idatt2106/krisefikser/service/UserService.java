package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PasswordChangeDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.UserMapper;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.model.User.Role;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

/**
 * Service class for managing user-related operations.
 * This class provides methods for creating, updating, deleting, and retrieving users.
 */
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
    public Optional<User> getUserById(int id) {
      return userRepository.findById(id);
    }

    /**
     * Delete a user by their ID.
     * @param id
     */
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    /**
     * Changes the password for a user.
     * @param id the ID of the user whose password will be changed
     * @param newPassword the new password (should be hashed before saving)
     * @return UserResponse object for the updated user
     */
    public UserResponseDTO changePassword(int id, PasswordChangeDTO password) {
      User existing = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
      existing.setPassword(password.getNewPassword()); // Remember to hash the password before saving
      User saved = userRepository.save(existing);
      return UserMapper.toResponseDTO(saved);
    }

    /**
     * Finds a user by their username.
     * @param username
     * @return User object if found, otherwise null
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by their email.
     * @param email
     * @return User object if found, otherwise null
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserResponseDTO> findAll() {
      return userRepository.findAll().stream()
          .map(UserMapper::toResponseDTO)
          .toList();

    }

    /**
     * Saves a user to the database.
     * @param user
     * @return User object that was saved
     */
    public UserResponseDTO saveUser(UserRequestDTO user) {
      User newUser = new User();
      newUser.setEmail(user.getEmail());
      newUser.setUsername(user.getUsername());
      newUser.setFirstName(user.getFirstName());
      newUser.setLastName(user.getLastName());
      
      Household household = householdService.findById(user.getHouseholdId()).orElseThrow(() -> new RuntimeException("Household not found"));
      newUser.setHousehold(household);

      newUser.setPassword(user.getPassword()); // Remember to hash the password before saving

      newUser.setRole(Role.normal);
      User savedUser = userRepository.save(newUser);
      return UserMapper.toResponseDTO(savedUser);


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
    public UserResponseDTO updateUser(int id, UserRequestDTO updated) {

      User existing = userRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("User not found " + id));
  
      if (updated.getUsername() != null)  existing.setUsername(updated.getUsername());
      if (updated.getEmail()    != null)  existing.setEmail(updated.getEmail());
      if (updated.getFirstName()!= null)  existing.setFirstName(updated.getFirstName());
      if (updated.getLastName() != null)  existing.setLastName(updated.getLastName());
      
      User saved = userRepository.save(existing);   // lagrer entiteten
  
      return UserMapper.toResponseDTO(saved); // mapper → DTO
    }

    /**
     * Checks if a user exists by their email.
     * @param email the email to check
     * @return true if a user with the given email exists, false otherwise
     */
    public boolean emailExists(String email) {
      return userRepository.existsByEmail(email);
    }

    /**
     * Checks if a user exists by their username.
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    public boolean usernameExists(String username) {
      return userRepository.existsByUsername(username);
    }
    

    
}

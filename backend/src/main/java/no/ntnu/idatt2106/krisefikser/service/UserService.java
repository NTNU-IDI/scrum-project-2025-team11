package no.ntnu.idatt2106.krisefikser.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PasswordChangeDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserUpdateDTO;
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
    private final PasswordEncoder passwordEncoder;

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
      newUser.setHousehold(householdService.findById(user.getHouseholdId())
            .orElseThrow(() -> new RuntimeException("Household not found")));
      
      newUser.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password before saving

      newUser.setRole(Role.normal);
      return UserMapper.toResponseDTO(userRepository.save(newUser)); // mapper → DTO

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
    public UserResponseDTO updateUser(int id, UserUpdateDTO updated) {

      User existing = userRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("User not found " + id));
  
      if (updated.getUsername() != null)  existing.setUsername(updated.getUsername());
      if (updated.getEmail()    != null)  existing.setEmail(updated.getEmail());
      if (updated.getFirstName()!= null)  existing.setFirstName(updated.getFirstName());
      if (updated.getLastName() != null)  existing.setLastName(updated.getLastName());
      
      User saved = userRepository.save(existing);   // lagrer entiteten
  
      return UserMapper.toResponseDTO(saved); // mapper → DTO
    }

    public UserResponseDTO changePassword(int id, PasswordChangeDTO dto) {
      User u = userRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

      // ✅ verify old password matches hashed password
      if (!passwordEncoder.matches(dto.getCurrentPassword(), u.getPassword())) {  // ③
          throw new RuntimeException("Old password is incorrect");
      }

      // 🔐 update via helper
      updatePassword(u, dto.getNewPassword());  // ④

      return UserMapper.toResponseDTO(u);
    }

    public void updatePassword(User user, String rawPassword) {
      user.setPassword(passwordEncoder.encode(rawPassword));
      userRepository.save(user);
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

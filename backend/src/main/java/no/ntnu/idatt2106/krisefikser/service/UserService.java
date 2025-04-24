package no.ntnu.idatt2106.krisefikser.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User save(User user) {
        return userRepository.save(user);
    }
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

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
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Hash the password before saving
          }
          if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
          }
    
          // Save the updated user entity
          return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
      }
    
}

package no.ntnu.idatt2106.krisefikser.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.ntnu.idatt2106.krisefikser.dto.UserRequest;
import no.ntnu.idatt2106.krisefikser.dto.UserResponse;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.model.User.Role;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

import no.ntnu.idatt2106.krisefikser.service.HouseholdService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private HouseholdService householdService;

    @InjectMocks
    private UserService userService;

    private Household defaultHousehold;

    @BeforeEach
    void setUp() {
        // Prepare a default household for mapping
        defaultHousehold = new Household();
        defaultHousehold.setId(42);
        when(householdService.findById(anyInt())).thenReturn(Optional.of(defaultHousehold));
    }

    /** Helper to create a fully populated User with a household */
    private User createUser(int id, String username, String email) {
        User u = new User();
        u.setId(id);
        u.setUsername(username);
        u.setEmail(email);
        u.setFirstName("First" + id);
        u.setLastName("Last" + id);
        u.setRole(Role.normal);
        u.setHousehold(defaultHousehold);
        return u;
    }

    @Test
    @DisplayName("saveUser maps DTO → entity, persists and returns response")
    void testSaveUser() {
        // Arrange – mock household lookup for save
        Household hh = new Household(); hh.setId(123);
        when(householdService.findById(123)).thenReturn(Optional.of(hh));

        when(userRepository.save(any(User.class)))
            .thenAnswer(inv -> { User u = inv.getArgument(0); u.setId(1); return u; });

        UserRequest req = new UserRequest();
        req.setUsername("Jonas");
        req.setEmail("jon@mail.com");
        req.setFirstName("Jonas");
        req.setLastName("Jonassen");
        req.setHouseholdId(123);

        // Act
        UserResponse resp = userService.saveUser(req);

        // Assert
        assertThat(resp.getId()).isEqualTo(1);
        assertThat(resp.getUsername()).isEqualTo("Jonas");
        assertThat(resp.getEmail()).isEqualTo("jon@mail.com");
        assertThat(resp.getRole()).isEqualTo(Role.normal.name());
        assertThat(resp.getHouseholdId()).isEqualTo(123);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("getUserById maps entity → response")
    void testGetUserById() {
        User u = createUser(10, "alice", "a@ex.com");
        when(userRepository.findById(10)).thenReturn(u);

        UserResponse out = userService.getUserById(10);

        assertThat(out.getUsername()).isEqualTo("alice");
        assertThat(out.getEmail()).isEqualTo("a@ex.com");
        assertThat(out.getHouseholdId()).isEqualTo(42);
    }

    @Test
    @DisplayName("updateUser updates selected fields and returns DTO")
    void testUpdateUser() {
        User existing = createUser(5, "old", "old@mail");
        when(userRepository.findById(5)).thenReturn(existing);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserRequest patch = new UserRequest();
        patch.setUsername("newName");
        patch.setEmail("new@mail.com");

        UserResponse updated = userService.updateUser(5, patch);

        assertThat(updated.getUsername()).isEqualTo("newName");
        assertThat(updated.getEmail()).isEqualTo("new@mail.com");
        assertThat(updated.getHouseholdId()).isEqualTo(42);
        verify(userRepository).save(existing);
    }

    @Test
    @DisplayName("updateUser throws when id not found")
    void testUpdateUserNotFound() {
        when(userRepository.findById(99)).thenReturn(null);

        UserRequest patch = new UserRequest();
        assertThatThrownBy(() -> userService.updateUser(99, patch))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("99");
    }

    @Test
    @DisplayName("findAll maps list of entities → list of DTOs")
    void testFindAll() {
        User u1 = createUser(1, "a", "a@b.c");
        User u2 = createUser(2, "b", "b@b.c");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserResponse> all = userService.findAll();

        assertThat(all).hasSize(2)
                       .extracting(UserResponse::getUsername)
                       .containsExactlyInAnyOrder("a", "b");
    }

    @Test
    @DisplayName("deleteAllUsers delegates to repository")
    void testDeleteAll() {
        userService.deleteAllUsers();
        verify(userRepository, times(1)).deleteAll();
    }
}

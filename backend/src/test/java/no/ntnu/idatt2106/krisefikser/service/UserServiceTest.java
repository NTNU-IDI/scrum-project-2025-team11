
    package no.ntnu.idatt2106.krisefikser.service;

    import static org.assertj.core.api.Assertions.*;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.*;

    import java.util.List;
    import java.util.Optional;

    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;
    import org.springframework.security.crypto.password.PasswordEncoder;

import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserUpdateDTO;
import no.ntnu.idatt2106.krisefikser.mapper.UserMapper;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.model.User.Role;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

    class UserServiceTest {

        @Mock  private UserRepository   userRepository;
        @Mock  private HouseholdService householdService;
        @Mock  private PasswordEncoder  passwordEncoder;

        @InjectMocks
        private UserService userService;



        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        @DisplayName("saveUser maps DTO → entity, persists and returns response")
        void testSaveUser() {
            // Arrange – mock household lookup
            Household hh = new Household();
            hh.setId(123);
            when(householdService.findById(123)).thenReturn(Optional.of(hh));
            when(passwordEncoder.encode(any())).thenReturn("mocked-hash");

            //  repo.save should return the same entity with generated id
            when(userRepository.save(any(User.class))).thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(1);       // mimic DB-generated PK
                return u;
            });

        UserRequestDTO req = new UserRequestDTO();
        req.setUsername("Jonas");
        req.setEmail("jon@mail.com");
        req.setFirstName("Jonas");
        req.setLastName("Jonassen");
        req.setHouseholdId(123);

        // Act
        UserResponseDTO resp = userService.saveUser(req, Role.normal);

        // Assert
        assertThat(resp.getId()).isEqualTo(1);
        assertThat(resp.getUsername()).isEqualTo("Jonas");
        assertThat(resp.getEmail()).isEqualTo("jon@mail.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("getUserById maps entity → response")
    void testGetUserById() {
        User u = new User();
        u.setId(10);
        u.setUsername("alice");
        u.setEmail("a@ex.com");
        u.setFirstName("Alice");
        u.setLastName("Wonder");
        u.setPassword("hash");
        u.setRole(Role.normal);
        u.setHousehold(new Household());
        u.getHousehold().setId(123);
        u.getHousehold().setName("Wonderland");

        when(userRepository.findById(10)).thenReturn(Optional.of(u));

        UserResponseDTO out = UserMapper.toResponseDTO(userService.getUserById(10)
                .orElseThrow(() -> new RuntimeException("User not found for id: 10")));

            assertThat(out.getUsername()).isEqualTo("alice");
            assertThat(out.getEmail()).isEqualTo("a@ex.com");
        }

    @Test
    @DisplayName("updateUser updates selected fields and returns DTO")
    void testUpdateUser() {
        // Existing user in DB
        User existing = new User();
        existing.setId(5);
        existing.setUsername("old");
        existing.setEmail("old@mail");
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setPassword("oldPwd");
        existing.setRole(Role.normal);
        existing.setHousehold(new Household());
        existing.getHousehold().setId(123);
        existing.getHousehold().setName("OldHouse");

        when(userRepository.findById(5)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserUpdateDTO patch = new UserUpdateDTO();
        patch.setUsername("newName");
        patch.setEmail("new@mail.com");

        UserResponseDTO updated = userService.updateUser(5, patch);

            assertThat(updated.getUsername()).isEqualTo("newName");
            assertThat(updated.getEmail()).isEqualTo("new@mail.com");
            verify(userRepository).save(existing);
        }

    @Test
    @DisplayName("updateUser throws when id not found")
    void testUpdateUserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        UserUpdateDTO patch = new UserUpdateDTO();
        assertThatThrownBy(() -> userService.updateUser(99, patch))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("findAll maps list of entities → list of DTOs")
    void testFindAll() {
        User u1 = new User(); u1.setId(1); u1.setUsername("a"); u1.setEmail("a@b.c"); u1.setRole(Role.normal); u1.setHousehold(new Household());
        User u2 = new User(); u2.setId(2); u2.setUsername("b"); u2.setEmail("b@b.c"); u2.setRole(Role.normal); u2.setHousehold(new Household());

            when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserResponseDTO> all = userService.findAll();

        assertThat(all).hasSize(2)
                       .extracting(UserResponseDTO::getUsername)
                       .containsExactlyInAnyOrder("a", "b");
    }

        @Test
        @DisplayName("deleteAllUsers delegates to repository")
        void testDeleteAll() {
            userService.deleteAllUsers();
            verify(userRepository, times(1)).deleteAll();
        }
    }



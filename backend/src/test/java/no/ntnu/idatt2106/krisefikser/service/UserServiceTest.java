
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

    import no.ntnu.idatt2106.krisefikser.dto.UserRequest;
    import no.ntnu.idatt2106.krisefikser.dto.UserResponse;
    import no.ntnu.idatt2106.krisefikser.model.Household;
    import no.ntnu.idatt2106.krisefikser.model.User;
    import no.ntnu.idatt2106.krisefikser.model.User.Role;
    import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

    class UserServiceTest {

        @Mock  private UserRepository   userRepository;
        @Mock  private HouseholdService householdService;

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

            //  repo.save should return the same entity with generated id
            when(userRepository.save(any(User.class))).thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(1);       // mimic DB-generated PK
                return u;
            });

            UserRequest req = new UserRequest();
            req.setUsername("Jonas");
            req.setEmail("jon@mail.com");
            req.setPassword("secret");
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

            when(userRepository.findById(10)).thenReturn(u);

            UserResponse out = userService.getUserById(10);

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

            when(userRepository.findById(5)).thenReturn(existing);
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            UserRequest patch = new UserRequest();
            patch.setUsername("newName");
            patch.setEmail("new@mail.com");

            UserResponse updated = userService.updateUser(5, patch);

            assertThat(updated.getUsername()).isEqualTo("newName");
            assertThat(updated.getEmail()).isEqualTo("new@mail.com");
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
            Household hh = new Household();
            User u1 = new User(); u1.setId(1); u1.setUsername("a"); u1.setEmail("a@b.c"); u1.setRole(Role.normal); u1.setHousehold(hh);
            User u2 = new User(); u2.setId(2); u2.setUsername("b"); u2.setEmail("b@b.c"); u2.setRole(Role.normal); u2.setHousehold(hh);

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



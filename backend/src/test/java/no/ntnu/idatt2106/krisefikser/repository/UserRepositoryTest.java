package no.ntnu.idatt2106.krisefikser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Household createAndSaveHousehold() {
        Address address = new Address();
        address.setStreet("Heiabakken 123");
        address.setPostalCode("1234");
        address.setCity("Trondheim");
        address.setLatitude(73.4305);
        address.setLongitude(18.5555);

        addressRepository.save(address);

        Household household = new Household();

        household.setName("Test Household");
        household.setMemberCount(4);
        household.setAddress(address);

        return household;
    }





    @Test
    @DisplayName("Create user")
    void createUser() {
        Household household = createAndSaveHousehold();
        householdRepository.save(household);
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setHousehold(household);
        // When
        User savedUser = userRepository.save(user);
        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@mail.com");
        assertThat(savedUser.getFirstName()).isEqualTo("Test");
        assertThat(savedUser.getLastName()).isEqualTo("User");
        assertThat(savedUser.getPassword()).isEqualTo("password");
    }
    
    @Test
    @DisplayName("Find user by username")
    void findByUsername() {
        Household household = createAndSaveHousehold();
        householdRepository.save(household);
        // Given
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("test@mail.com");
        user1.setPassword("password");
        user1.setFirstName("Test");
        user1.setLastName("User");
        user1.setHousehold(household);
        userRepository.save(user1);
        // When
        User foundUser = userRepository.findByUsername("user1");
        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("user1");
        assertThat(foundUser.getEmail()).isEqualTo("test@mail.com");
        assertThat(foundUser.getFirstName()).isEqualTo("Test");
        assertThat(foundUser.getLastName()).isEqualTo("User");
        assertThat(foundUser.getPassword()).isEqualTo("password");
        assertThat(foundUser.getHousehold()).isNotNull();
    }

}



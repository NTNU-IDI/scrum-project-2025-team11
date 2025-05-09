package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HouseholdRepositoryTest {

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private AddressRepository addressRepository; // <-- You'll need this too

    private Address createAndSaveAddress() {
        Address address = new Address();
        address.setStreet("Heiabakken 123");
        address.setPostalCode("1234");
        address.setCity("Trondheim");
        address.setLatitude(73.4305);
        address.setLongitude(18.5555);
        return addressRepository.save(address);
    }

    @Test
    @DisplayName("Test save household with address")
    void testSaveHousehold() {
        Address address = createAndSaveAddress();

        Household household = new Household();
        household.setName("Test Family");
        household.setMemberCount(4);
        household.setAddress(address);

        Household savedHousehold = householdRepository.save(household);

        assertThat(savedHousehold.getId()).isNotNull();
        assertThat(savedHousehold.getName()).isEqualTo("Test Family");
        assertThat(savedHousehold.getAddress()).isNotNull();
        assertThat(savedHousehold.getAddress().getCity()).isEqualTo("Trondheim");
    }

    @Test
    @DisplayName("Test find household by id")
    void testFindHouseholdById() {
        Address address = createAndSaveAddress();

        Household household = new Household();
        household.setName("Another Family");
        household.setMemberCount(3);
        household.setAddress(address);

        Household savedHousehold = householdRepository.save(household);

        Optional<Household> found = householdRepository.findById(savedHousehold.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Another Family");
        assertThat(found.get().getAddress().getStreet()).isEqualTo("Heiabakken 123");
    }

    @Test
    @DisplayName("Test find all households")
    void testFindAllHouseholds() {
        Address address = createAndSaveAddress();

        Household household1 = new Household();
        household1.setName("Family 1");
        household1.setMemberCount(2);
        household1.setAddress(address);

        Household household2 = new Household();
        household2.setName("Family 2");
        household2.setMemberCount(5);
        household2.setAddress(address);

        householdRepository.save(household1);
        householdRepository.save(household2);

        List<Household> households = householdRepository.findAll();

        // Expect 4 because there already exist 2 tests in the DB
        assertThat(households).hasSize(4);
    }

    @Test
    @DisplayName("Test delete household by id")
    void testDeleteHouseholdById() {
        Address address = createAndSaveAddress();

        Household household = new Household();
        household.setName("Delete Family");
        household.setMemberCount(1);
        household.setAddress(address);

        Household savedHousehold = householdRepository.save(household);

        householdRepository.deleteById(savedHousehold.getId());

        Optional<Household> deleted = householdRepository.findById(savedHousehold.getId());

        assertThat(deleted).isNotPresent();
    }

    @Test
    @Transactional
    void deleteOrphanHouseholds_onlyRemovesUnreferenced() {
        // GIVEN a household with and one without users
        Household keep = new Household();
        keep.setName("Keep");
        em.persist(keep);

        Household orphan = new Household();
        orphan.setName("Orphan");
        em.persist(orphan);

        User u = new User();
        u.setEmail("a@b.com");
        u.setUsername("user");
        u.setFirstName("F");
        u.setLastName("L");
        u.setPassword("pw");
        u.setHousehold(keep);
        em.persist(u);

        em.flush();       // assign IDs & store rows
        em.clear();       // ensure repo.findById sees real DB

        // WHEN
        int deleted = householdRepository.deleteOrphanHouseholds();

        // THEN only the orphan is removed
        assertThat(deleted).isEqualTo(1);
        assertThat(householdRepository.findById(keep.getId())).isPresent();
        assertThat(householdRepository.findById(orphan.getId())).isEmpty();
    }
}

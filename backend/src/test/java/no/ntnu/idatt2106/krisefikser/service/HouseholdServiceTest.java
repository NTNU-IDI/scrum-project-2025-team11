package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdUpdateDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class HouseholdServiceTest {

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private HouseholdService householdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test find household by ID")
    void testFindById() {
        Household household = new Household();
        household.setId(1);
        household.setName("Test Household");

        when(householdRepository.findById(1)).thenReturn(Optional.of(household));

        Optional<Household> found = householdService.findById(1);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Household");
    }

    @Test
    @DisplayName("Test update household")
    void testUpdateHousehold() {
        Household household = new Household();
        household.setId(1);
        household.setName("Old Name");
        household.setMemberCount(2);

        when(householdRepository.findById(1)).thenReturn(Optional.of(household));
        when(householdRepository.save(ArgumentMatchers.any(Household.class))).thenAnswer(i -> i.getArgument(0));

        HouseholdUpdateDTO updateDTO = new HouseholdUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setMemberCount(3);

        HouseholdResponseDTO updated = householdService.updateHousehold(1, updateDTO);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getMemberCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test save new household")
    void testSaveHousehold() throws Exception {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        addressResponseDTO.setId(1);

        Address address = new Address();
        address.setId(1);
        address.setCity("Trondheim");

        when(addressService.save(ArgumentMatchers.any(AddressRequestDTO.class))).thenReturn(addressResponseDTO);
        when(addressService.findById(1)).thenReturn(Optional.of(address));

        when(householdRepository.save(ArgumentMatchers.any(Household.class))).thenAnswer(i -> {
            Household h = i.getArgument(0);
            h.setId(1); // mimic generated ID
            return h;
        });

        HouseholdRequestDTO householdRequestDTO = new HouseholdRequestDTO();
        householdRequestDTO.setName("New Household");
        householdRequestDTO.setMemberCount(5);
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setCity("Trondheim");
        addressRequestDTO.setStreet("Street 1");
        addressRequestDTO.setPostalCode("1234");
        addressRequestDTO.setLatitude(73.0);
        addressRequestDTO.setLongitude(18.0);
        householdRequestDTO.setAddress(addressRequestDTO);

        HouseholdResponseDTO saved = householdService.save(householdRequestDTO);

        assertThat(saved.getName()).isEqualTo("New Household");
        assertThat(saved.getMemberCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("Test save household throws exception when missing fields")
    void testSaveHouseholdMissingFields() {
        HouseholdRequestDTO badRequest = new HouseholdRequestDTO();
        badRequest.setName(null); // Name missing
        badRequest.setMemberCount(0); // MemberCount missing

        assertThatThrownBy(() -> householdService.save(badRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name is missing");
    }

    @Test
    @DisplayName("Test exists by id")
    void testExistsById() {
        when(householdRepository.existsById(1)).thenReturn(true);

        boolean exists = householdService.existsById(1);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Test delete household by id")
    void testDeleteById() {
        householdService.deleteById(1);

        verify(householdRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Test find all households")
    void testFindAll() {
        Household household1 = new Household();
        household1.setId(1);
        household1.setName("Family A");
        household1.setMemberCount(4);

        Household household2 = new Household();
        household2.setId(2);
        household2.setName("Family B");
        household2.setMemberCount(5);

        when(householdRepository.findAll()).thenReturn(List.of(household1, household2));

        List<HouseholdResponseDTO> all = householdService.findAll();

        assertThat(all).hasSize(2);
    }
}

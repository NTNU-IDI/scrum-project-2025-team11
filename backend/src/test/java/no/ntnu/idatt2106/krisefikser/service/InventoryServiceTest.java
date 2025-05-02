package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.dto.UpsertInventoryRequest;
import no.ntnu.idatt2106.krisefikser.mapper.HouseholdItemMapper;
import no.ntnu.idatt2106.krisefikser.model.*;
import no.ntnu.idatt2106.krisefikser.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock private HouseholdRepository hhRepo;
    @Mock private ItemRepository itemRepo;
    @Mock private HouseholdItemRepository hiRepo;
    @Mock private HouseholdItemMapper mapper;

    @InjectMocks private InventoryServiceImpl service;

    private Household household;
    private Item item;
    private UpsertInventoryRequest upsertReq;
    private HouseholdItemRequest updateReq;

    @BeforeEach
    void setUp() {
        household = new Household();
        household.setId(1);

        item = new Item();
        item.setId(100);
        item.setName("TestItem");

        upsertReq = new UpsertInventoryRequest();
        upsertReq.setItemId(100);
        upsertReq.setQuantity(new BigDecimal("5"));
        upsertReq.setUnit("pcs");
        upsertReq.setAcquiredDate(LocalDateTime.of(2025,5,2,10,0));
        upsertReq.setExpirationDate(LocalDate.of(2025,5,10));

        updateReq = new HouseholdItemRequest();
        updateReq.setItemId(100);
        updateReq.setQuantity(new BigDecimal("3"));
        updateReq.setUnit("kg");
        updateReq.setAcquiredDate(LocalDateTime.of(2025,5,2,10,0));
        updateReq.setExpirationDate(LocalDate.of(2025,5,15));
    }

    @Test
    void testUpsert_existingItem_savesAndReturnsResponse() {
        when(hhRepo.findById(1)).thenReturn(Optional.of(household));
        when(itemRepo.findById(100)).thenReturn(Optional.of(item));

        HouseholdItemId expectedId = new HouseholdItemId(1, 100, upsertReq.getAcquiredDate());
        HouseholdItem hiEntity = new HouseholdItem(
            expectedId, household, item,
            upsertReq.getQuantity(), upsertReq.getUnit(), upsertReq.getExpirationDate()
        );
        when(mapper.toResponse(any())).thenReturn(
            new HouseholdItemResponse(1, 100, upsertReq.getQuantity(), upsertReq.getUnit(), upsertReq.getAcquiredDate(), upsertReq.getExpirationDate())
        );

        // Perform
        HouseholdItemResponse response = service.upsert(1, upsertReq);

        // Verify
        ArgumentCaptor<HouseholdItem> captor = ArgumentCaptor.forClass(HouseholdItem.class);
        verify(hiRepo).save(captor.capture());
        HouseholdItem saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(expectedId);
        assertThat(response.getHouseholdId()).isEqualTo(1);
        assertThat(response.getItemId()).isEqualTo(100);
    }

    @Test
    void testRemove_existingEntry_deletesSuccessfully() {
        LocalDateTime ts = updateReq.getAcquiredDate();
        HouseholdItemId id = new HouseholdItemId(1, 100, ts);
        when(hiRepo.existsById(id)).thenReturn(true);

        // No exception means success
        service.remove(1, 100, ts);

        verify(hiRepo).deleteById(id);
    }

    @Test
    void testRemove_nonExisting_throwsException() {
        LocalDateTime ts = updateReq.getAcquiredDate();
        HouseholdItemId id = new HouseholdItemId(1, 100, ts);
        when(hiRepo.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.remove(1, 100, ts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Inventory entry not found");
    }
}

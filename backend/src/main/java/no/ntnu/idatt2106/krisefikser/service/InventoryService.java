package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;

import java.time.LocalDate;
import java.util.List;

public interface InventoryService {
    List<HouseholdItemResponse> list(Integer householdId);
    HouseholdItemResponse add(Integer householdId, HouseholdItemRequest req);
    HouseholdItemResponse update(Integer householdId, Integer itemId, HouseholdItemRequest req);
    void remove(Integer householdId, Integer itemId, LocalDate acquiredDate);
}
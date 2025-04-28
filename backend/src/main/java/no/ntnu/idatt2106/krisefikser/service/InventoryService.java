package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.dto.UpsertInventoryRequest;

import java.time.LocalDate;
import java.util.List;

public interface InventoryService {
    List<HouseholdItemResponse> list(Integer householdId);
    HouseholdItemResponse upsert(Integer householdId, UpsertInventoryRequest req);
    HouseholdItemResponse add(Integer householdId, HouseholdItemRequest req);
    HouseholdItemResponse update(Integer householdId, Integer itemId, HouseholdItemRequest req);
    void remove(Integer householdId, Integer itemId, LocalDate acquiredDate);
}
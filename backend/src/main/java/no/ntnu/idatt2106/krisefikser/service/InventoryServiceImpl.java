package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.dto.UpsertInventoryRequest;
import no.ntnu.idatt2106.krisefikser.mapper.HouseholdItemMapper;
import no.ntnu.idatt2106.krisefikser.model.*;
import no.ntnu.idatt2106.krisefikser.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final HouseholdRepository hhRepo;
    private final ItemRepository itemRepo;
    private final HouseholdItemRepository hiRepo;
    private final HouseholdItemMapper mapper;

    public InventoryServiceImpl(HouseholdRepository hhRepo,
                                ItemRepository itemRepo,
                                HouseholdItemRepository hiRepo,
                                HouseholdItemMapper mapper) {
        this.hhRepo = hhRepo;
        this.itemRepo = itemRepo;
        this.hiRepo = hiRepo;
        this.mapper = mapper;
    }

    @Override
    public List<HouseholdItemResponse> list(Integer householdId) {
        return hiRepo.findByHouseholdId(householdId).stream()
                     .map(mapper::toResponse)
                     .collect(Collectors.toList());
    }

    @Override
    public HouseholdItemResponse add(Integer householdId, HouseholdItemRequest req) {
        Household hh = hhRepo.findById(householdId)
            .orElseThrow(() -> new IllegalArgumentException("Household not found"));
        Item item = itemRepo.findById(req.getItemId())
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        var hi = mapper.toEntity(householdId, req, hh, item);
        return mapper.toResponse(hiRepo.save(hi));
    }

    @Override
    public HouseholdItemResponse update(Integer householdId, Integer itemId, HouseholdItemRequest req) {
        var id = new HouseholdItemId(householdId, itemId, req.getAcquiredDate());
        HouseholdItem hi = hiRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Inventory entry not found"));
        hi.setQuantity(req.getQuantity());
        hi.setUnit(req.getUnit());
        hi.setExpirationDate(req.getExpirationDate());
        return mapper.toResponse(hiRepo.save(hi));
    }

    @Override
    public HouseholdItemResponse upsert(Integer householdId, UpsertInventoryRequest req) {
        // Validate household
        Household hh = hhRepo.findById(householdId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Household not found"));

        // Find or create master Item
        Item item;
        if (req.getItemId() != null) {
            item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item not found"));
        } else {
            item = new Item();
            item.setName(req.getName());
            item.setDescription(req.getDescription());
            item = itemRepo.save(item);
        }

        // Create inventory entry
        HouseholdItemId pk = new HouseholdItemId(
            householdId,
            item.getId(),
            req.getAcquiredDate()
        );
        HouseholdItem hi = new HouseholdItem(
            pk, hh, item,
            req.getQuantity(),
            req.getUnit(),
            req.getExpirationDate()
        );
        hi = hiRepo.save(hi);

        return mapper.toResponse(hi);
    }

    @Override
    public void remove(Integer householdId, Integer itemId, LocalDate acquiredDate) {
        // Fully qualify the composite key, including the purchase date
        var id = new HouseholdItemId(householdId, itemId, acquiredDate);
        if (!hiRepo.existsById(id)) {
            throw new IllegalArgumentException("Inventory entry not found");
        }
        hiRepo.deleteById(id);
    }

    @Override
    public void removeAll(Integer householdId, Integer itemId) {
        var all = hiRepo.findByHouseholdIdAndItemId(householdId, itemId);
        if (all.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No purchases found");
        }
        hiRepo.deleteAll(all);
}
}
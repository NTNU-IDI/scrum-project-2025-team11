package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(Integer id);
    Item create(Item item);
    Item update(Integer id, Item item);
    void delete(Integer id);
}
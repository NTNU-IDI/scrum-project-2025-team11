package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.model.Item;
import no.ntnu.idatt2106.krisefikser.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing items in the system.
 * Provides methods to perform CRUD operations on items.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repo;

    /**
     * Constructor for ItemServiceImpl.
     *
     * @param repo the ItemRepository to be used for data access
     */
    public ItemServiceImpl(ItemRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all items from the repository.
     *
     * @return a list of all items
     */
    @Override
    public List<Item> findAll() {
        return repo.findAll();
    }

    /**
     * Retrieves an item by its ID.
     *
     * @param id the ID of the item to retrieve
     * @return an Optional containing the item if found, or empty if not found
     */
    @Override
    public Optional<Item> findById(Integer id) {
        return repo.findById(id);
    }

    /**
     * Creates a new item in the repository.
     *
     * @param item the item to create
     * @return the created item
     */
    @Override
    public Item create(Item item) {
        // Validations
        return repo.save(item);
    }

    /**
     * Updates an existing item in the repository.
     *
     * @param id   the ID of the item to update
     * @param item the updated item data
     * @return the updated item
     */
    @Override
    public Item update(Integer id, Item item) {
        item.setId(id);
        return repo.save(item);
    }

    /**
     * Deletes an item by its ID.
     *
     * @param id the ID of the item to delete
     */
    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
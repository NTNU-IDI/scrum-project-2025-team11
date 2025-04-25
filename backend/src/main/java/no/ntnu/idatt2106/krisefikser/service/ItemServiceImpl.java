package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.model.Item;
import no.ntnu.idatt2106.krisefikser.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repo;

    public ItemServiceImpl(ItemRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Item> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Item create(Item item) {
        // you might add validation or defaults here
        return repo.save(item);
    }

    @Override
    public Item update(Integer id, Item item) {
        item.setId(id);
        return repo.save(item);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
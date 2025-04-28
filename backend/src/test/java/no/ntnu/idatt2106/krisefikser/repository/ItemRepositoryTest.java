package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.Item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository repo;

    @Test
    void saveAndRetrieve() {
        // given
        Item i = new Item();
        i.setName("Test");
        i.setDescription("Desc");
        Item saved = repo.save(i);

        // when
        var found = repo.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test");
    }

    @Test
    void findAllContainsNewItems() {
        repo.save(new Item(null, "A", "a"));
        repo.save(new Item(null, "B", "b"));

        List<Item> all = repo.findAll();
        assertThat(all).extracting(Item::getName)
                       .containsExactlyInAnyOrder("A", "B");
    }
}

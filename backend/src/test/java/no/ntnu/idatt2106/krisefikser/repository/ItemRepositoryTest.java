package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItem;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItemId;
import no.ntnu.idatt2106.krisefikser.model.Item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ItemRepositoryTest {
    
    @Autowired
    private ItemRepository repo;
    
    @Autowired
    private EntityManager em;
    
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
    
    @Test
    @Transactional
    void deleteOrphanItems_onlyRemovesUnreferenced() {
        // ── given ──
        // 1) create two items: one that will be referenced, one that will be orphan
        Item referenced = new Item(null, "Referenced", "Should stay");
        Item orphan     = new Item(null, "Orphan",     "Should be deleted");
        em.persist(referenced);
        em.persist(orphan);
        
        // 2) create a Household
        Household hh = new Household();
        hh.setName("TestHouse");
        em.persist(hh);
        
        // Flush now so referenced.getId(), orphan.getId() and hh.getId() are all non-null
        em.flush();
        
        // 3) build the HouseholdItem key using the now-assigned IDs
        LocalDateTime acquired = LocalDateTime.now();
        HouseholdItemId key = new HouseholdItemId(
        hh.getId(),
        referenced.getId(),
        acquired
        );
        
        // 4) link only the 'referenced' item
        HouseholdItem hi = new HouseholdItem(
        key,
        hh,
        referenced,
        new BigDecimal("1.00"),
        "pcs",
        LocalDate.now()
        );
        em.persist(hi);
        
        // ensure the join row is in the DB
        em.flush();
        
        // ── when ──
        int deletedCount = repo.deleteOrphanItems();
        em.flush();
        
        // ── then ──
        assertThat(deletedCount).isEqualTo(1);
        
        // the referenced item still exists...
        assertThat(repo.findById(referenced.getId())).isPresent();
        
        // ...but the orphan is gone
        assertThat(repo.findById(orphan.getId())).isEmpty();
    }
    
    
}

package no.ntnu.idatt2106.krisefikser.scheduling;

import no.ntnu.idatt2106.krisefikser.repository.ItemRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduled task for cleaning up orphan items.
 * This task runs every day at 02:05 in the Europe/Oslo timezone.
 * It deletes any items that are not referenced by any household items.
 * The task is marked as transactional to ensure that the database operations are performed atomically.
 * The task is also marked with @Component to allow Spring to manage it as a bean.
 */
@Component
public class OrphanItemCleanupTask {

    private final ItemRepository itemRepository;

    public OrphanItemCleanupTask(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Runs every day at 02:00 in the Europe/Oslo timezone.
     */
    @Scheduled(cron = "0 05 2 * * *", zone = "Europe/Oslo")
    @Transactional
    public void purgeOrphanItems() {
        int deleted = itemRepository.deleteOrphanItems();
        System.out.println("Orphan-cleanup: deleted " + deleted + " items at 02:05");
    }
}

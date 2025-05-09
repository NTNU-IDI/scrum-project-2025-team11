package no.ntnu.idatt2106.krisefikser.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdRepository;


/**
 * Scheduled task for cleaning up orphan households.
 * This task runs every day at 02:00 in the Europe/Oslo timezone.
 * It deletes any households that have no users associated with them.
 * The task is marked as transactional to ensure that the database operations are performed atomically.
 * The task is also marked with @Component to allow Spring to manage it as a bean.
 */
@Component
public class OrphanHouseholdCleanupTask {

    private final HouseholdRepository householdRepo;

    public OrphanHouseholdCleanupTask(HouseholdRepository householdRepo) {
        this.householdRepo = householdRepo;
    }

    /**
     * Runs every day at 02:00 Europe/Oslo time, deleting households with no users.
     */
    @Scheduled(cron = "0 0 2 * * *", zone = "Europe/Oslo")
    @Transactional
    public void purgeOrphanHouseholds() {
        int deleted = householdRepo.deleteOrphanHouseholds();
        System.out.println("Orphan-household cleanup: deleted " + deleted + " households at 02:00");
    }
}

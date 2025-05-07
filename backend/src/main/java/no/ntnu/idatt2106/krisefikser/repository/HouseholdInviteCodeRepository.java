package no.ntnu.idatt2106.krisefikser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.HouseholdInviteCode;

public interface HouseholdInviteCodeRepository extends JpaRepository<HouseholdInviteCode, Integer> {
    Optional<HouseholdInviteCode> findByCode(String code);
    void deleteByHousehold(Household household);
    
}

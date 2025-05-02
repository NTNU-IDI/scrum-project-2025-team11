package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.idatt2106.krisefikser.model.TwoFactorCode;

@Repository
public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Integer> {

}

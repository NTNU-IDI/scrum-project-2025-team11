package no.ntnu.idatt2106.krisefikser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.idatt2106.krisefikser.model.TwoFactorCode;
import no.ntnu.idatt2106.krisefikser.model.User;

@Repository
public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Integer> {

    Optional<TwoFactorCode> findByCode(String code);
    void deleteByUser(User user);

}

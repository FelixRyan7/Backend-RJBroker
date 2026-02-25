package com.broker.wallet_service.repository;

import com.broker.wallet_service.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsByUserId(Long userId);

    Optional<Wallet> findByUserId(Long userId);

    @Query("select distinct w.userId from Wallet w")
    List<Long> findDistinctUserIds();

}

package com.broker.wallet_service.repository;

import com.broker.wallet_service.model.PortfolioSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioSnapshotRepository extends JpaRepository<PortfolioSnapshot, Long> {

    // buscar todos los snapshots de un usuario ordenados por snapshotDate asc
    List<PortfolioSnapshot> findByUserIdOrderBySnapshotDateAsc(Long userId);
    Optional<PortfolioSnapshot> findByUserIdAndSnapshotDate(Long userId, LocalDate snapshotDate);
}
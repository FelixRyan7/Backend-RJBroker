package com.broker.wallet_service.repository;

import com.broker.wallet_service.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUserIdAndAssetSymbol(Long userId, String assetSymbol);

    List<Portfolio> findAllByUserId(Long userId);
}
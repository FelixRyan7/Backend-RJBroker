package com.broker.asset_service.repository;

import com.broker.asset_service.model.Asset;
import com.broker.asset_service.model.AssetType;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByType(AssetType type, Pageable pageable);




}


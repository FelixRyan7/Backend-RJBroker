package com.broker.wallet_service.service;

import com.broker.wallet_service.model.PortfolioSnapshot;
import com.broker.wallet_service.repository.PortfolioSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioSnapshotService {

    @Autowired
    private PortfolioSnapshotRepository snapshotRepository;

    public List<PortfolioSnapshot> getUserSnapshots(Long userId) {
        return snapshotRepository.findByUserIdOrderBySnapshotDateAsc(userId);
    }
    public Optional<PortfolioSnapshot> getSnapshotByDate(Long userId, LocalDate snapshotDate) {
        return snapshotRepository.findByUserIdAndSnapshotDate(userId, snapshotDate);
    }
}

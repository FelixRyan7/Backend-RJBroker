package com.broker.wallet_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "portfolio_snapshots")
public class PortfolioSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDate snapshotDate;
    private double totalValue;
    private double cash;

    public PortfolioSnapshot() {}

    public PortfolioSnapshot(Long userId, LocalDate snapshotDate, double totalValue, double cash) {
        this.userId = userId;
        this.snapshotDate = snapshotDate;
        this.totalValue = totalValue;
        this.cash = cash;
    }

    // getters setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public LocalDate getSnapshotDate() { return snapshotDate; }
    public double getTotalValue() { return totalValue; }
    public double getCash() { return cash; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setSnapshotDate(LocalDate snapshotDate) { this.snapshotDate = snapshotDate; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }
    public void setCash(double cash) { this.cash = cash; }
}


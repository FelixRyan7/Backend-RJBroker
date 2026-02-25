package com.broker.wallet_service.DTOS;

import java.time.LocalDate;
import java.util.List;

public class GenerateReportRequest {
    private LocalDate startSnapshotDate;
    private double netWorth;
    private double cash;
    private List<FullAssetData> assets;

    // getters y setters

    public LocalDate getStartSnapshotDate() {
        return startSnapshotDate;
    }

    public void setStartSnapshotDate(LocalDate startSnapshotDate) {
        this.startSnapshotDate = startSnapshotDate;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public List<FullAssetData> getAssets() {
        return assets;
    }

    public void setAssets(List<FullAssetData> assets) {
        this.assets = assets;
    }
}

package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;
import java.util.List;

public class WalletSumaryResponse {
    BigDecimal balance;
    String currency;
    private List<PortfolioAssetResponse> assets;

    public WalletSumaryResponse(BigDecimal balance, String currency, List<PortfolioAssetResponse> assets) {
        this.balance = balance;
        this.currency = currency;
        this.assets = assets;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<PortfolioAssetResponse> getAssets() {
        return assets;
    }

    public void setAssets(List<PortfolioAssetResponse> assets) {
        this.assets = assets;
    }
}

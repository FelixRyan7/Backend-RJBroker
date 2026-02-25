package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;

public class PortfolioAssetResponse {
    private Long assetId;
    private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal averagePrice;

    public PortfolioAssetResponse(Long assetId, String assetSymbol, BigDecimal quantity, BigDecimal averagePrice) {
        this.assetId = assetId;
        this.assetSymbol = assetSymbol;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    // getters y setters

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }
}

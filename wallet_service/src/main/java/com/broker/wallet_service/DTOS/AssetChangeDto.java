package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;

public class AssetChangeDto {
    private String symbol;
    private String category;
    private double quantity;
    private BigDecimal totalAmount; // total invertido o retirado en esa operación

    public AssetChangeDto(String symbol, double quantity, BigDecimal totalAmount, String category) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.category = category;
    }

    // Getters y setters...
}

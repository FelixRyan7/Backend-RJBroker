package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;

public class AssetDto {
    private Long id;
    private String symbol;
    private String name;
    private String logo;
    private String type;       // STOCK, ETF, etc.
    private String category;   // Insurance, Technology, etc.
    private double quantity;
    private BigDecimal averagePrice;  // precio promedio de compra
    private BigDecimal currentPrice;  // precio actual

    // Constructor
    public AssetDto(Long id, String symbol, String name, String logo, String type, String category,
                    double quantity, BigDecimal averagePrice, BigDecimal currentPrice) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.category = category;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.currentPrice = currentPrice;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getLogo() { return logo; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getQuantity() { return quantity; }
    public BigDecimal getAveragePrice() { return averagePrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }

    public void setId(Long id) { this.id = id; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setName(String name) { this.name = name; }
    public void setLogo(String logo) { this.logo = logo; }
    public void setType(String type) { this.type = type; }
    public void setCategory(String category) { this.category = category; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
}

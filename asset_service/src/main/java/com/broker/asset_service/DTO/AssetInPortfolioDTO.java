package com.broker.asset_service.DTO;

import com.broker.asset_service.model.AssetType;

import java.math.BigDecimal;

public class AssetInPortfolioDTO {
    private Long id;           // number -> Long
    private String symbol;     // string -> String
    private String name;       // string -> String
    private String logo;       // string -> String (URL o path)
    private AssetType type;       // string -> String
    private BigDecimal currentPrice;

    private String category;

    public AssetInPortfolioDTO(Long id, String symbol, String name, String logo, AssetType type, BigDecimal currentPrice, String category) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.currentPrice = currentPrice;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }



    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

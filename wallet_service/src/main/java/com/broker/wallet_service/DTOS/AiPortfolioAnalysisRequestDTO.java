package com.broker.wallet_service.DTOS;

import java.util.List;

public class AiPortfolioAnalysisRequestDTO {

    private int assetCount;
    private List<PortfolioAssetDTO> assets;
    private double cash;
    private String currency;
    private double totalValueInLocalCurrency;
    private int typeCount;

    // Constructor vacío (necesario para @RequestBody)
    public AiPortfolioAnalysisRequestDTO() {}

    // Constructor completo opcional
    public AiPortfolioAnalysisRequestDTO(int assetCount, List<PortfolioAssetDTO> assets, double cash,
                                         String currency, double totalValueInLocalCurrency, int typeCount) {
        this.assetCount = assetCount;
        this.assets = assets;
        this.cash = cash;
        this.currency = currency;
        this.totalValueInLocalCurrency = totalValueInLocalCurrency;
        this.typeCount = typeCount;
    }

    // Getters y setters
    public int getAssetCount() { return assetCount; }
    public void setAssetCount(int assetCount) { this.assetCount = assetCount; }

    public List<PortfolioAssetDTO> getAssets() { return assets; }
    public void setAssets(List<PortfolioAssetDTO> assets) { this.assets = assets; }

    public double getCash() { return cash; }
    public void setCash(double cash) { this.cash = cash; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getTotalValueInLocalCurrency() { return totalValueInLocalCurrency; }
    public void setTotalValueInLocalCurrency(double totalValueInLocalCurrency) { this.totalValueInLocalCurrency = totalValueInLocalCurrency; }

    public int getTypeCount() { return typeCount; }
    public void setTypeCount(int typeCount) { this.typeCount = typeCount; }

    // Inner DTO de asset
    public static class PortfolioAssetDTO {
        private String symbol;
        private String name;
        private String type;
        private double quantity;
        private double averagePrice;
        private Double currentPrice;
        private Double value;
        private Double allocationPercent;

        public PortfolioAssetDTO() {}

        public PortfolioAssetDTO(String symbol, String name, String type, double quantity, double averagePrice,
                                 Double currentPrice, Double value, Double allocationPercent) {
            this.symbol = symbol;
            this.name = name;
            this.type = type;
            this.quantity = quantity;
            this.averagePrice = averagePrice;
            this.currentPrice = currentPrice;
            this.value = value;
            this.allocationPercent = allocationPercent;
        }

        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public double getQuantity() { return quantity; }
        public void setQuantity(double quantity) { this.quantity = quantity; }

        public double getAveragePrice() { return averagePrice; }
        public void setAveragePrice(double averagePrice) { this.averagePrice = averagePrice; }

        public Double getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }

        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }

        public Double getAllocationPercent() { return allocationPercent; }
        public void setAllocationPercent(Double allocationPercent) { this.allocationPercent = allocationPercent; }
    }
}



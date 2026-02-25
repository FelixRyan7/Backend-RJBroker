package com.broker.wallet_service.DTOS;

public class AssetsDataForAiDto {

    private String symbol;
    private String name;

    // valor actual del asset en el portfolio (price * quantity)
    double valueEUR;
    private String category;
    private double weightPercent;

    public AssetsDataForAiDto() {}

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

    public double getValueEUR() {
        return valueEUR;
    }

    public void setValueEUR(double valueEUR) {
        this.valueEUR = valueEUR;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getWeightPercent() {
        return weightPercent;
    }

    public void setWeightPercent(double weightPercent) {
        this.weightPercent = weightPercent;
    }
}



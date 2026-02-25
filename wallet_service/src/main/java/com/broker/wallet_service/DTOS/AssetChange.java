package com.broker.wallet_service.DTOS;

public class AssetChange {
    int quantity;
    double totalAmount;

    public void addQuantity(int q) {
        this.quantity += q;
    }

    public void addAmount(double amount) {
        this.totalAmount += amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
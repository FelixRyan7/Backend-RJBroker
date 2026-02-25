package com.broker.wallet_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "asset_prices")
public class AssetPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assetSymbol;

    private LocalDate date; // fecha de mercado
    private double closePrice;

    public AssetPrice() {}

    public AssetPrice(String assetSymbol, LocalDate date, double closePrice) {
        this.assetSymbol = assetSymbol;
        this.date = date;
        this.closePrice = closePrice;
    }

    // getters setters
    public Long getId() { return id; }
    public String getAssetSymbol() { return assetSymbol; }
    public LocalDate getDate() { return date; }
    public double getClosePrice() { return closePrice; }

    public void setId(Long id) { this.id = id; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setClosePrice(double closePrice) { this.closePrice = closePrice; }
}


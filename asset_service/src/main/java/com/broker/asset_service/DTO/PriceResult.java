package com.broker.asset_service.DTO;

public record PriceResult(
        Double priceUsd,
        Double change24h
) {}

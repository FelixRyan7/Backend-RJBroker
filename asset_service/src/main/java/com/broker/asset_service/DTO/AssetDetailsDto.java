package com.broker.asset_service.DTO;

import java.math.BigDecimal;

public record AssetDetailsDto(
        Long id,
        String name,
        String symbol,
        BigDecimal price,
        BigDecimal open,
        BigDecimal change,
        BigDecimal changePercent,
        String logo,

        String description,
        String currency,
        BigDecimal marketCap,
        BigDecimal open5D,
        BigDecimal open1M,
        BigDecimal open1Y,
        String isin,
        String market,
        BigDecimal dividendAmount,
        BigDecimal peRatio,
        BigDecimal eps,
        BigDecimal ebitda,
        BigDecimal netIncome

) {}
package com.broker.asset_service.DTO;

import java.math.BigDecimal;

public record AssetFinancialsDto(
        String description,
        BigDecimal dividendAmount,
        BigDecimal marketCap,
        BigDecimal peRatio,
        BigDecimal eps,
        BigDecimal ebitda,
        BigDecimal netIncome
) {}

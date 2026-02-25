package com.broker.asset_service.DTO;

import java.math.BigDecimal;

public record CriptoDetailsDto (
        Long id,
        String name,
        String symbol,
        BigDecimal price,
        BigDecimal open,
        BigDecimal change,
        BigDecimal changePercent,
        String logo,
        String description,
        BigDecimal marketCap,
        BigDecimal open5D,
        BigDecimal open1M,
        BigDecimal open1Y
) {}



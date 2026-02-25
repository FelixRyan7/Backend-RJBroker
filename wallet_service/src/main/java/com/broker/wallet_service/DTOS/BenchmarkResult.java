package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;

public record BenchmarkResult(
        String symbol,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        BigDecimal performancePercent
) {}

package com.broker.asset_service.service;



import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AssetHistoricalService {

    private final PoligonClient polygonClient;

    public AssetHistoricalService(PoligonClient polygonClient) {
        this.polygonClient = polygonClient;
    }

    public HistoricalOpenPrices getHistoricalOpenPrices(String symbol) {
        LocalDate today = LocalDate.now();

        BigDecimal open5D = polygonClient.getOpenPrice(symbol, today.minusDays(5));
        BigDecimal open1M = polygonClient.getOpenPrice(symbol, today.minusDays(30));
        BigDecimal open1Y = polygonClient.getOpenPrice(symbol, today.minusDays(365));

        return new HistoricalOpenPrices(open5D, open1M, open1Y);
    }

    public static class HistoricalOpenPrices {
        public final BigDecimal open5D;
        public final BigDecimal open1M;
        public final BigDecimal open1Y;

        public HistoricalOpenPrices(BigDecimal open5D, BigDecimal open1M, BigDecimal open1Y) {
            this.open5D = open5D;
            this.open1M = open1M;
            this.open1Y = open1Y;
        }
    }
}

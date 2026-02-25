package com.broker.wallet_service.service;

import com.broker.wallet_service.DTOS.AssetPriceDto;
import com.broker.wallet_service.DTOS.BenchmarkResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BenchMarkService {

    private final PoligonClient poligonClient;
    private final FinnhubClient finnhubClient;

    public BenchMarkService(PoligonClient poligonClient, FinnhubClient finnhubClient) {
        this.poligonClient = poligonClient;
        this.finnhubClient = finnhubClient;
    }

    public BenchmarkResult getBenchmark(String symbol, LocalDate startDate) {

        System.out.println(startDate);
        BigDecimal startPrice = poligonClient.getOpenPrice(symbol, startDate);
        AssetPriceDto quote = finnhubClient.getQuote(symbol);
        BigDecimal currentPrice = quote.price();

        BigDecimal performance = BigDecimal.ZERO;

        if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) > 0) {
            performance = currentPrice
                    .subtract(startPrice)
                    .divide(startPrice, 6, BigDecimal.ROUND_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return new BenchmarkResult(symbol, startPrice, currentPrice, performance);
    }

    public List<BenchmarkResult> getBenchmarks(LocalDate startDate) {
        return List.of(
                getBenchmark("SPY", startDate),
                getBenchmark("QQQ", startDate),
                getBenchmark("VGK", startDate),
                getBenchmark("URTH", startDate)
        );
    }
}

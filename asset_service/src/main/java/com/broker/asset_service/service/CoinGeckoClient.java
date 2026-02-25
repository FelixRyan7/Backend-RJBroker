package com.broker.asset_service.service;

import com.broker.asset_service.DTO.CryptoPriceDataDto;
import com.broker.asset_service.DTO.PriceResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CoinGeckoClient {

    private static final String API_KEY = "CG-muyaEEDAhy5bZuwBS81KEARL";
    private static final String BASE_URL =
            "https://api.coingecko.com/api/v3";

    private final RestTemplate restTemplate = new RestTemplate();

    public CryptoPriceDataDto getCryptoData(String symbol) {

        String coinId = mapSymbolToCoinGeckoId(symbol);

        // 1️⃣ Precio actual + change 24h
        Map<String, Map<String, Double>> priceResponse = simplePrice(coinId);

        BigDecimal price = BigDecimal.valueOf(((Number)priceResponse.get(coinId).get("usd")).doubleValue());
        BigDecimal changePercentBd = BigDecimal.valueOf(((Number)priceResponse.get(coinId).get("usd_24h_change")).doubleValue());


        BigDecimal open24h = price.divide(
                BigDecimal.ONE.add(changePercentBd.divide(BigDecimal.valueOf(100), 8, BigDecimal.ROUND_HALF_UP)),
                8, BigDecimal.ROUND_HALF_UP
        );

        BigDecimal change = price.subtract(open24h);

        // 2️⃣ Históricos
        BigDecimal open5D = getHistoricalOpen(coinId, 5);
        BigDecimal open1M = getHistoricalOpen(coinId, 30);
        BigDecimal open1Y = getHistoricalOpen(coinId, 365);

        return new CryptoPriceDataDto(
                price, open24h, change, changePercentBd,
                open5D, open1M, open1Y
        );
    }

    private Map<String, Map<String, Double>> simplePrice(String coinId) {
        String url = BASE_URL + "/simple/price?ids=" + coinId +
                "&vs_currencies=usd&include_24hr_change=true";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", API_KEY);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class
        );

        return response.getBody();
    }

    private BigDecimal getHistoricalOpen(String coinId, int days) {

        String url = BASE_URL + "/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days + "&interval=daily";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", API_KEY);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class
        );

        Map<String, List<List<Double>>> body = response.getBody();

        if (body == null || body.get("prices") == null || body.get("prices").isEmpty()) {
            return BigDecimal.ZERO;
        }

        // El primer precio del array es el "open" del período
        Double open = body.get("prices").get(0).get(1);
        return BigDecimal.valueOf(open);
    }

    private String mapSymbolToCoinGeckoId(String symbol) {
        return switch (symbol.toUpperCase()) {
            case "BTC" -> "bitcoin";
            case "ETH" -> "ethereum";
            case "BNB" -> "binancecoin";
            case "USDT" -> "tether";
            case "ADA" -> "cardano";
            case "DOT" -> "polkadot";
            case "SOL" -> "solana";
            case "UNI" -> "uniswap";
            case "CAKE" -> "pancakeswap-token";
            default -> throw new IllegalArgumentException("Unsupported symbol: " + symbol);
        };
    }
}

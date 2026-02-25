package com.broker.asset_service.service;

import com.broker.asset_service.DTO.AssetFinancialsDto;
import com.broker.asset_service.DTO.AssetPriceDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class AlphaVantageClient {

    private final String apiKey = "VQSR07DEDN72DM6C";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene el precio actual de una acción o ETF
     * @param symbol ejemplo: "AAPL" o "SPY"
     * @return precio como String
     */
    public AssetPriceDto getCurrentPrice(String symbol) {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                + symbol + "&apikey=" + apiKey;

        try {
            // Llamada HTTP GET
            var response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode quote = root.path("Global Quote");
            System.out.println(response);

            if (quote.isMissingNode()) {
                throw new RuntimeException("No quote data for symbol " + symbol);
            }


            return new AssetPriceDto(
                    quote.path("01. symbol").asText(),
                    new BigDecimal(quote.path("05. price").asText("0")),
                    new BigDecimal(quote.path("02. open").asText("0")),
                    new BigDecimal(quote.path("08. previous close").asText("0")),
                    new BigDecimal(quote.path("09. change").asText("0")),
                    quote.path("10. change percent").asText()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error calling AlphaVantage", e);
        }
    }

    public AssetFinancialsDto getCompanyOverview(String symbol) {
        String url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol="
                + symbol + "&apikey=" + apiKey;

        try {
            var response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (root.isEmpty() || root.has("Note")) {
                throw new RuntimeException("No overview data for symbol " + symbol);
            }
            System.out.println(response);

            return new AssetFinancialsDto(
                    root.path("Description").asText().isEmpty() ? "No description available" : root.path("Description").asText(),
                    parseSafe(root.path("DividendPerShare"), BigDecimal.ZERO),
                    parseSafe(root.path("MarketCapitalization"), BigDecimal.valueOf(1_000_000_000)),
                    parseSafe(root.path("PERatio"), BigDecimal.valueOf(15)),
                    parseSafe(root.path("EPS"), BigDecimal.valueOf(2.5)),
                    parseSafe(root.path("EBITDA"), BigDecimal.valueOf(5_000_000)),
                    parseSafe(root.path("RevenueTTM"), BigDecimal.valueOf(10_000_000))
            );

        } catch (Exception e) {
            throw new RuntimeException("Error calling AlphaVantage Overview", e);
        }
    }

    private BigDecimal parseSafe(JsonNode node, BigDecimal fallback) {
        try {
            String text = node.asText();
            if (text == null || text.isEmpty() || text.equals("0")) return fallback;
            return new BigDecimal(text);
        } catch (Exception e) {
            return fallback;
        }
    }

}


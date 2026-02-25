package com.broker.asset_service.service;

import com.broker.asset_service.DTO.AssetFinancialsDto;
import com.broker.asset_service.DTO.AssetPriceDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FinnhubClient {

    private final String apiKey = "d62aejpr01qlugepaepgd62aejpr01qlugepaeq0";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AssetPriceDto getQuote(String symbol) {
        String url = "https://finnhub.io/api/v1/quote?symbol="
                + symbol + "&token=" + apiKey;

        JsonNode root = get(url);

        return new AssetPriceDto(
                symbol,
                root.path("c").decimalValue(),
                root.path("o").decimalValue(),
                root.path("pc").decimalValue(),
                root.path("d").decimalValue(),
                root.path("dp").asText()
        );
    }

    public AssetFinancialsDto getFinancials(String symbol) {
        String url = "https://finnhub.io/api/v1/stock/metric?symbol="
                + symbol + "&metric=all&token=" + apiKey;

        JsonNode root = get(url).path("metric");

        return new AssetFinancialsDto(
                "", // description (viene en profile)
                root.path("dividendPerShareTTM").decimalValue(),
                root.path("marketCapitalization").decimalValue(),
                root.path("peNormalizedAnnual").decimalValue(),
                root.path("epsNormalizedAnnual").decimalValue(),
                root.path("ebitda").decimalValue(),
                root.path("netIncome").decimalValue()
        );
    }

    public String getDescription(String symbol) {
        String url = "https://finnhub.io/api/v1/stock/profile?symbol="
                + symbol + "&token=" + apiKey;

        JsonNode root = get(url);
        return root.path("description").asText("No description available");
    }

    public String getLogo(String symbol) {
        String url = "https://finnhub.io/api/v1/stock/profile2?symbol="
                + symbol + "&token=" + apiKey;

        JsonNode root = get(url);
        return root.path("logo").asText(""); // si no hay logo, devuelve ""
    }


    private JsonNode get(String url) {
        try {
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Finnhub error", e);
        }
    }
}


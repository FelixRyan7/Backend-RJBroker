package com.broker.wallet_service.service;

import com.broker.wallet_service.DTOS.AssetPriceDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

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

    private JsonNode get(String url) {
        try {
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Finnhub error", e);
        }
    }
}

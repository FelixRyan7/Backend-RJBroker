package com.broker.wallet_service.DTOS;

import java.util.List;

/**
 * DTO principal para enviar requests a Gemini
 */
public record GeminiRequest(
        List<Content> contents
) {
    public record Content(
            List<Part> parts
    ) {
        public record Part(
                String text
        ) {}
    }
}

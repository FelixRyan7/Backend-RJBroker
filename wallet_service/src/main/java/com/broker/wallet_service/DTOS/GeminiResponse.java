package com.broker.wallet_service.DTOS;

import java.util.List;

/**
 * DTO principal para recibir responses de Gemini
 */
public record GeminiResponse(
        List<Candidate> candidates
) {
    public record Candidate(
            Content content
    ) {
        public record Content(
                List<Part> parts
        ) {
            public record Part(
                    String text
            ) {}
        }
    }
}

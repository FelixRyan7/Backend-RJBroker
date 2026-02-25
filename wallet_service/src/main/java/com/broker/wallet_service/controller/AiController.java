package com.broker.wallet_service.controller;

import com.broker.wallet_service.DTOS.*;
import com.broker.wallet_service.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/asset/term")
    public ResponseEntity<String> analyzeTerm(@RequestBody AiTermRequest request) {

        String prompt;

        if ("dividendAmount".equals(request.termKey())) {
            // Prompt especial para dividendAmount
            prompt = """
                       Explain the dividend amount for the asset %s (%s) in simple terms for beginners.
                       The current dividend is: %s per year in local currency of the company.
                       Keep your explanation concise and limit it to a maximum of 3 short paragraphs.
                       Highlight why this matters for long-term investors and how it can affect potential returns.
                    """.formatted(
                    request.assetName(),
                    request.assetSymbol(),
                    request.value()
            );
        } else if ("description".equals(request.termKey())) {
            prompt = """
            Provide a clear and beginner-friendly overview of the company %s (%s).

            Do not reuse or reference any provided description. Write your own original explanation.
            Briefly describe what the company does, its main business model, and where most of its revenue or profits come from.

            Keep the explanation concise and limit it to a maximum of 3 short paragraphs.
            Focus on clarity and practical understanding for retail investors.
            """.formatted(
                    request.assetName(),
                    request.assetSymbol()
            );
        } else if ("marketCap".equals(request.termKey())) {
            prompt = """
            Explain the market capitalization of the asset %s (%s) in simple terms for beginners.
            The current market cap is: %s in local currency.

            Explain what this tells us about the company's size and stability.
            Mention how company size can influence risk and growth potential.
            Keep your explanation concise and limit it to a maximum of 3 short paragraphs.
    """.formatted(
                    request.assetName(),
                    request.assetSymbol(),
                    request.value()
            );
        } else if ("eps".equals(request.termKey())) {
            prompt = """
        Explain the earnings per share (EPS) of the asset %s (%s) in simple terms for beginners.
        The current EPS is: %s in local currency.

        Explain what EPS represents and why it matters for profitability.
        Mention how investors use EPS to assess company performance and valuation.
        Keep your explanation concise and limit it to a maximum of 3 short paragraphs.
    """.formatted(
                    request.assetName(),
                    request.assetSymbol(),
                    request.value()
            );
        } else {
            // Prompt genérico para otros metrics
            prompt = """
            Explain the financial metric "%s" for the asset %s (%s).
            The current value is: %s.
            Explain what this means for an investor in simple terms.
            Keep your explanation concise and limit it to a maximum of 3 short paragraphs.
        """.formatted(
                    request.termKey(),
                    request.assetName(),
                    request.assetSymbol(),
                    request.value()
            );
        }

        // Aquí llamás a tu servicio de AI con el prompt
        String aiResponse = aiService.analyzeTerm(prompt);

        return ResponseEntity.ok(aiResponse);
    }

    @PostMapping("/asset/question")
    public ResponseEntity<String> analyzeQuestion(@RequestBody AiQuestionRequest request) {
        // request.question(), request.assetName(), etc.

        String prompt = """
        You are a financial information assistant.

        The user is asking about the company %s (%s).

        Your rules:
        - Only provide general information about this specific company.
        - Only answer questions directly related to this company.
        - Do NOT provide financial advice.
        - Do NOT give buy, sell, or investment recommendations.
        - Do NOT predict future stock performance.
        - Do NOT compare it with other companies unless strictly necessary to explain context.
        - Do NOT invent or assume data.
        - If information is unknown, clearly state that it is not available.

        Keep your answer concise (maximum 3 short paragraphs).
        Use simple language suitable for beginner investors.

        User question:
        %s
        """.formatted(
                request.assetName(),
                request.assetSymbol(),
                request.question()
        );

        String aiResponse = aiService.analyzeTerm(prompt);
        return ResponseEntity.ok(aiResponse);
    }

        @PostMapping("portfolio/Analysis")
        public ResponseEntity<String> analyzePortfolio(@RequestBody AiPortfolioAnalysisRequestDTO request) {
            StringBuilder sb = new StringBuilder();
            sb.append("You are a financial assistant.\n\n");
            sb.append("The user has the following portfolio:\n");
            sb.append("Cash: ").append(request.getCash()).append(" ").append(request.getCurrency()).append("\n");
            sb.append("Total value: ").append(request.getTotalValueInLocalCurrency()).append(" ").append(request.getCurrency()).append("\n\n");
            sb.append("Assets:\n");
            for (AiPortfolioAnalysisRequestDTO.PortfolioAssetDTO asset : request.getAssets()) {
                sb.append("- ").append(asset.getQuantity())
                        .append(" ").append(asset.getSymbol())
                        .append(" ").append(asset.getName())
                        .append(" (").append(asset.getType()).append(")\n");
            }

            sb.append("""
                    \nYour rules:
                      - Explain in simple terms to the user what their portfolio consists of in the first paragrpah.
                          For example, highlight sectors, asset types, and any significant exposure (e.g., technology, automotive, cryptocurrencies).
                      - Analyze diversification by type, sector, and geography.
                      - Explain the risks and volatility associated with the portfolio.
                      - Point out if the portfolio is concentrated or has good balance.
                      - Provide educational notes to help the user understand their exposure and potential implications of their asset allocation.
                      - Do NOT give buy, sell, or investment recommendations.
                      - Keep the language simple and beginner-friendly.
                      - Structure your answer as concise paragraphs, maximum 5.
                    """);
            String prompt = sb.toString();
            String aiResponse = aiService.analyzeTerm(prompt);
            return ResponseEntity.ok(aiResponse);
        }

    @PostMapping("/portfolio/question")
    public ResponseEntity<String> analyzePortfolioQuestion(@RequestBody AiPortfolioQuestionRequestDTO request) {

        String prompt = buildPrompt(request.getQuestion(), request.getPortfolio());
        String aiResponse = aiService.analyzeTerm(prompt);
        return ResponseEntity.ok(aiResponse);
    }

    private String buildPrompt(String question, AiPortfolioAnalysisRequestDTO portfolio) {

        StringBuilder sb = new StringBuilder();
        sb.append("The user has the following investment portfolio:\n");
        sb.append("Cash: ").append(portfolio.getCash())
                .append(" ").append(portfolio.getCurrency()).append("\n");
        sb.append("Total value: ").append(portfolio.getTotalValueInLocalCurrency())
                .append(" ").append(portfolio.getCurrency()).append("\n");
        sb.append("Assets:\n");
        for (AiPortfolioAnalysisRequestDTO.PortfolioAssetDTO asset : portfolio.getAssets()) {
            sb.append("- ").append(asset.getQuantity())
                    .append(" ").append(asset.getSymbol())
                    .append(" (").append(asset.getType()).append(")\n");
        }


        String prompt = """
        You are a financial education assistant.

        Here is the user's portfolio:
        %s

        Rules:
        - Answer the user's question based on this portfolio.
        - Explain in simple terms the composition, risks, and exposure.
        - Only provide general information; do NOT give financial advice.
        - Do NOT give buy, sell, or investment recommendations.
        - Keep your answer concise and beginner-friendly (maximum 2 short paragraphs).

        User question:
        %s
        """.formatted(
                sb.toString(),
                question
        );

        return prompt;
    }



}


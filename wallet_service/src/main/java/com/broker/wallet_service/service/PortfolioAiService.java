package com.broker.wallet_service.service;

import com.broker.wallet_service.DTOS.AssetsDataForAiDto;
import com.broker.wallet_service.DTOS.PortfolioReportDto;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class PortfolioAiService {
    private final AiService aiService;

    public PortfolioAiService(AiService aiService) {
        this.aiService = aiService;
    }

    public Map<String,Object> generatePortfolioSummary(PortfolioReportDto report) {

        String prompt = buildPrompt(report);

        // Analiza con IA y devuelve texto JSON
        String jsonResponse = aiService.analyzeTerm(prompt);
        jsonResponse = cleanJson(jsonResponse);
        // 👇 ver respuesta real de Gemini
        System.out.println("===== GEMINI RAW RESPONSE =====");
        System.out.println(jsonResponse);
        System.out.println("================================");

        // Convertimos el JSON devuelto a Map
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonResponse, new TypeReference<Map<String,Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error parsing AI JSON response", e);
        }
    }
    private String buildPrompt(PortfolioReportDto report) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
                You are a portfolio analysis assistant.

                GOAL:
                Explain the portfolio evolution to the investor (not to an analyst) .
                The investor is beginner-intermediate.

                STYLE:
                - Clear
                - Educational
                - Natural language
                - No technical jargon
                - Explain what it means for the investor

                STRICT RULES:
                1. portfolio positions = CurrentAssets only
                2. benchmarks are NOT portfolio assets
                3. NEVER include benchmarks in allocation/exposure calculations
                4. Benchmarks are ONLY for performance comparison, this comparison should explain : - if the portfolio outperformed or underperformed the relevant markets
                                                                                                    - possible reasons (allocation, concentration, sector exposure)
                5. DO NOT mention benchmark tickers
                6. Instead, refer to the markets they represent:

                SPY → US broad market
                QQQ → US technology market
                VGK → European market
                URTH → Global market

                CALCULATION RULES (MANDATORY):
                - Each asset value = quantity * price
                - Portfolio allocation MUST be calculated using asset value and NetWorthCurrent
                - You MUST compute percentages by:
                   • country
                   • category(in case of crypto just category must be just "crypto")
                - DO NOT say you cannot calculate percentages (you have the data)
                
                PERFORMANCE RULES:
                Performance is already calculated:
                - totalGainLoss = real performance excluding cash flows
                - percentGainLoss = percentage performance
                Use these values to explain performance.
                Do NOT recalculate performance unless necessary for explanation.
                

                 ANALYSIS RULES:
                  - Explain how the portfolio changed
                  - Explain what the investor is favoring
                  - Compare performance vs relevant markets (using market names, not ETFs)
                  - Describe risks and potential benefits
                  - Detect direction (more tech, more diversification, concentration, etc.)
                  - ASSET COVERAGE (VERY IMPORTANT):
                  You MUST review the full CurrentAssets list BEFORE writing do not ignore crypto assets.
                  All sections MUST reflect the real current portfolio composition.
                  Do NOT focus only on moved assets.
                  Use category to describe diversification and allocation instead of country.
                  Use valueEUR and weightPercent for all calculations.
                  Do NOT invent assets, categories, or allocations.
                  - Mention the most relevant current holdings when explaining allocation and portfolio direction.
                  Highlight concentration if one asset, country or category dominates the portfolio or if one asset is very volatile.

                 OUTPUT:
                   Return ONLY valid JSON with 4 string fields:
                 {
                    "portfolioOverview": "",
                    "Risk And Concentration": "",
                    "investorProfile": "",
                    "portfolioTrendAnalysis": ""
                 }

                 DATA:
                 """);
        sb.append("ReportPeriod from :").append(report.getSnapshotDate()).append("to: ").append(report.getReportDate()).append("\n");
        sb.append("NetWorthCurrent: ").append(report.getNetWorthCurrent()).append("\n");
        sb.append("NetWorthSnapshot: ").append(report.getNetWorthSnapshot()).append("\n");
        sb.append("CashAdjustment: ").append(report.getCashAdjustment()).append("\n");
        sb.append("total gain or loss in this period: ").append(report.getTotalGainLoss()).append("\n");

        sb.append("Movements:\n");
        sb.append("incorporations: ").append(report.getIncorporations()).append("\n");
        sb.append("increments: ").append(report.getIncrements()).append("\n");
        sb.append("decrements: ").append(report.getDecrements()).append("\n");
        sb.append("removals: ").append(report.getRemovals()).append("\n");

        sb.append("CURRENT_ASSETS_SOURCE_OF_TRUTH:\n");
        for (AssetsDataForAiDto a : report.getCurrentAssets()) {
            sb.append(String.format(
                    "Asset: symbol=%s, name=%s, category=%s, valueEUR=%.2f, weightPercent=%.2f\n",
                    a.getSymbol(), a.getName(), a.getCategory(), a.getValueEUR(), a.getWeightPercent()
            ));
        }

        sb.append("Benchmarks (comparison only): ").append(report.getBenchmarks()).append("\n");
        sb.append("InvestorPerformancePercent: ").append(report.getPercentGainLoss()).append("\n");
        sb.append("Use this value to compare portfolio performance vs the relevant markets from benchmarks.\n");
        return sb.toString();
    }
    private String cleanJson(String text){
        if(text == null) return null;

        // quita ```json ```
        text = text.replace("```json", "")
                .replace("```", "")
                .trim();

        return text;
    }
}

package com.broker.wallet_service.DTOS;

public class AiPortfolioQuestionRequestDTO {
    private String question;
    private AiPortfolioAnalysisRequestDTO portfolio;

    // getters y setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public AiPortfolioAnalysisRequestDTO getPortfolio() { return portfolio; }
    public void setPortfolio(AiPortfolioAnalysisRequestDTO portfolio) { this.portfolio = portfolio; }
}

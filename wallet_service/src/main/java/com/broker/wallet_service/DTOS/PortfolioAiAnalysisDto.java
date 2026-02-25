package com.broker.wallet_service.DTOS;

public class PortfolioAiAnalysisDto {
    private String portfolioOverview;
    private String assetAllocation;
    private String investorProfile;
    private String portfolioTrendAnalysis;

    public String getPortfolioOverview() {
        return portfolioOverview;
    }

    public void setPortfolioOverview(String portfolioOverview) {
        this.portfolioOverview = portfolioOverview;
    }

    public String getAssetAllocation() {
        return assetAllocation;
    }

    public void setAssetAllocation(String assetAllocation) {
        this.assetAllocation = assetAllocation;
    }

    public String getInvestorProfile() {
        return investorProfile;
    }

    public void setInvestorProfile(String investorProfile) {
        this.investorProfile = investorProfile;
    }

    public String getPortfolioTrendAnalysis() {
        return portfolioTrendAnalysis;
    }

    public void setPortfolioTrendAnalysis(String portfolioTrendAnalysis) {
        this.portfolioTrendAnalysis = portfolioTrendAnalysis;
    }
}

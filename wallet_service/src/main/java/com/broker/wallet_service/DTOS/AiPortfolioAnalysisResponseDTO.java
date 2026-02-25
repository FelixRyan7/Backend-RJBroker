package com.broker.wallet_service.DTOS;

import java.util.List;
import java.util.Map;

public class AiPortfolioAnalysisResponseDTO {

    private String summary; // resumen general del portafolio
    private Composition composition;
    private Map<String, String> risksAndBenefits; // tipo de activo -> explicación
    private List<String> educationalNotes;

    // Constructor vacío (para Spring)
    public AiPortfolioAnalysisResponseDTO() {}

    // Constructor completo opcional
    public AiPortfolioAnalysisResponseDTO(String summary, Composition composition,
                                          Map<String, String> risksAndBenefits,
                                          List<String> educationalNotes) {
        this.summary = summary;
        this.composition = composition;
        this.risksAndBenefits = risksAndBenefits;
        this.educationalNotes = educationalNotes;
    }

    // Getters y setters
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Composition getComposition() { return composition; }
    public void setComposition(Composition composition) { this.composition = composition; }

    public Map<String, String> getRisksAndBenefits() { return risksAndBenefits; }
    public void setRisksAndBenefits(Map<String, String> risksAndBenefits) { this.risksAndBenefits = risksAndBenefits; }

    public List<String> getEducationalNotes() { return educationalNotes; }
    public void setEducationalNotes(List<String> educationalNotes) { this.educationalNotes = educationalNotes; }

    // Clase interna Composition
    public static class Composition {
        private Map<String, Double> byType;      // % por tipo de activo
        private Map<String, Double> bySector;    // % por sector (opcional)
        private Map<String, Double> byGeography; // % por región (opcional)

        public Composition() {}

        public Composition(Map<String, Double> byType, Map<String, Double> bySector, Map<String, Double> byGeography) {
            this.byType = byType;
            this.bySector = bySector;
            this.byGeography = byGeography;
        }

        public Map<String, Double> getByType() { return byType; }
        public void setByType(Map<String, Double> byType) { this.byType = byType; }

        public Map<String, Double> getBySector() { return bySector; }
        public void setBySector(Map<String, Double> bySector) { this.bySector = bySector; }

        public Map<String, Double> getByGeography() { return byGeography; }
        public void setByGeography(Map<String, Double> byGeography) { this.byGeography = byGeography; }
    }
}


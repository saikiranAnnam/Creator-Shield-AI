package com.creatorshield.validation;

import com.creatorshield.domain.RiskLevel;
import com.creatorshield.domain.ValidationDecision;
import java.util.List;

public record TrustEvaluation(
        int trustScore,
        RiskLevel riskLevel,
        ValidationDecision decision,
        List<String> reasonCodes,
        Double duplicateScore,
        Double titleSimilarityScore) {}

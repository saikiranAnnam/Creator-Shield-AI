package com.creatorshield.dto;

import com.creatorshield.domain.ValidationResult;
import java.time.Instant;
import java.util.List;

public record ValidationResultResponse(
        long uploadId,
        Double duplicateScore,
        Double titleSimilarityScore,
        int trustScore,
        String riskLevel,
        String decision,
        List<String> reasonCodes,
        Instant validatedAt) {

    public static ValidationResultResponse from(ValidationResult vr) {
        return new ValidationResultResponse(
                vr.getUpload().getId(),
                vr.getDuplicateScore(),
                vr.getTitleSimilarityScore(),
                vr.getTrustScore(),
                vr.getRiskLevel().name(),
                vr.getDecision().name(),
                vr.getReasonCodes(),
                vr.getValidatedAt());
    }
}

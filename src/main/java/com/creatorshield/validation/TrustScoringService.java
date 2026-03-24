package com.creatorshield.validation;

import com.creatorshield.domain.Creator;
import com.creatorshield.domain.RiskLevel;
import com.creatorshield.domain.Upload;
import com.creatorshield.domain.ValidationDecision;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TrustScoringService {

    private static final double TITLE_SIMILARITY_THRESHOLD = 0.9;
    private static final int UPLOAD_RATE_WINDOW_HOURS = 1;
    private static final int UPLOAD_RATE_THRESHOLD = 5;
    private static final int NEW_ACCOUNT_DAYS = 7;
    private static final double FLAG_RATE_THRESHOLD = 0.3;

    public TrustEvaluation evaluate(
            Upload upload, DuplicateSignals duplicate, Creator creator, long uploadsLastHour) {
        List<String> reasons = new ArrayList<>();
        int score = 0;

        if (duplicate.exactDuplicateOtherCreator()) {
            score += 30;
            reasons.add(ReasonCode.EXACT_DUPLICATE_FOUND);
        }

        if (duplicate.maxTitleSimilarity() >= TITLE_SIMILARITY_THRESHOLD) {
            score += 20;
            reasons.add(ReasonCode.HIGH_TITLE_SIMILARITY);
        }

        double flagRate =
                creator.getTotalUploads() == 0
                        ? 0.0
                        : (double) creator.getFlaggedUploads() / creator.getTotalUploads();
        if (flagRate >= FLAG_RATE_THRESHOLD && creator.getTotalUploads() >= 3) {
            score += 15;
            reasons.add(ReasonCode.CREATOR_HISTORY_RISK);
        }

        Instant sevenDaysAgo = Instant.now().minus(NEW_ACCOUNT_DAYS, ChronoUnit.DAYS);
        if (creator.getAccountCreatedAt().isAfter(sevenDaysAgo)) {
            score += 10;
            reasons.add(ReasonCode.NEW_ACCOUNT);
        }

        if (uploadsLastHour > UPLOAD_RATE_THRESHOLD) {
            score += 10;
            reasons.add(ReasonCode.NEW_ACCOUNT_HIGH_UPLOAD_RATE);
        }

        if (isMetadataThin(upload)) {
            score += 5;
            reasons.add(ReasonCode.METADATA_INCOMPLETE);
        }

        if (creator.getTrustHistoryScore() >= 75 && creator.getTotalUploads() >= 5) {
            score -= 10;
            reasons.add(ReasonCode.STRONG_CREATOR_HISTORY);
        }

        score = Math.max(0, Math.min(100, score));

        RiskLevel risk = mapRisk(score);
        ValidationDecision decision = mapDecision(risk);

        return new TrustEvaluation(score, risk, decision, reasons, duplicate.duplicateScore(), duplicate.maxTitleSimilarity());
    }

    private static boolean isMetadataThin(Upload upload) {
        return upload.getAlbumName() == null
                || upload.getAlbumName().isBlank()
                || upload.getGenre() == null
                || upload.getGenre().isBlank();
    }

    private static RiskLevel mapRisk(int score) {
        if (score < 40) {
            return RiskLevel.LOW;
        }
        if (score < 70) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.HIGH;
    }

    private static ValidationDecision mapDecision(RiskLevel risk) {
        return switch (risk) {
            case LOW -> ValidationDecision.AUTO_APPROVED;
            case MEDIUM -> ValidationDecision.REVIEW_REQUIRED;
            case HIGH -> ValidationDecision.SUSPICIOUS_ALERT;
        };
    }
}

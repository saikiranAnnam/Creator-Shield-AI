package com.creatorshield.service;

import com.creatorshield.domain.AlertRecordStatus;
import com.creatorshield.domain.Creator;
import com.creatorshield.domain.SuspiciousAlert;
import com.creatorshield.domain.Upload;
import com.creatorshield.domain.UploadStatus;
import com.creatorshield.domain.ValidationDecision;
import com.creatorshield.domain.ValidationResult;
import com.creatorshield.repository.CreatorRepository;
import com.creatorshield.repository.SuspiciousAlertRepository;
import com.creatorshield.repository.UploadRepository;
import com.creatorshield.repository.ValidationResultRepository;
import com.creatorshield.validation.DuplicateCheckService;
import com.creatorshield.validation.TrustEvaluation;
import com.creatorshield.validation.TrustScoringService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UploadValidationService {

    private final UploadRepository uploadRepository;
    private final ValidationResultRepository validationResultRepository;
    private final SuspiciousAlertRepository suspiciousAlertRepository;
    private final CreatorRepository creatorRepository;
    private final DuplicateCheckService duplicateCheckService;
    private final TrustScoringService trustScoringService;

    @Transactional
    public void validateUpload(long uploadId) {
        Upload upload = uploadRepository.findByIdWithCreator(uploadId).orElseThrow();
        if (validationResultRepository.findFetchedByUploadId(uploadId).isPresent()) {
            return;
        }

        Creator creator =
                creatorRepository.findById(upload.getCreator().getId()).orElseThrow();

        long uploadsLastHour =
                uploadRepository.countByCreator_IdAndCreatedAtAfter(
                        creator.getId(), Instant.now().minus(1, ChronoUnit.HOURS));

        var duplicate = duplicateCheckService.analyze(upload);
        TrustEvaluation eval = trustScoringService.evaluate(upload, duplicate, creator, uploadsLastHour);

        ValidationResult result =
                ValidationResult.builder()
                        .upload(upload)
                        .duplicateScore(eval.duplicateScore())
                        .titleSimilarityScore(eval.titleSimilarityScore())
                        .trustScore(eval.trustScore())
                        .riskLevel(eval.riskLevel())
                        .decision(eval.decision())
                        .reasonCodes(new ArrayList<>(eval.reasonCodes()))
                        .validatedAt(Instant.now())
                        .build();
        validationResultRepository.save(result);

        upload.setUploadStatus(mapUploadStatus(eval.decision()));
        uploadRepository.save(upload);

        applyCreatorFeedback(creator, eval);

        if (eval.decision() == ValidationDecision.SUSPICIOUS_ALERT) {
            suspiciousAlertRepository.save(
                    SuspiciousAlert.builder()
                            .upload(upload)
                            .alertType("HIGH_TRUST_RISK")
                            .alertMessage(String.join(", ", eval.reasonCodes()))
                            .status(AlertRecordStatus.OPEN)
                            .createdAt(Instant.now())
                            .build());
        }
    }

    private static UploadStatus mapUploadStatus(ValidationDecision decision) {
        return switch (decision) {
            case AUTO_APPROVED -> UploadStatus.VALIDATED_APPROVED;
            case REVIEW_REQUIRED -> UploadStatus.VALIDATED_REVIEW;
            case SUSPICIOUS_ALERT -> UploadStatus.VALIDATED_SUSPICIOUS;
        };
    }

    private void applyCreatorFeedback(Creator creator, TrustEvaluation eval) {
        if (eval.decision() == ValidationDecision.SUSPICIOUS_ALERT) {
            creator.setFlaggedUploads(creator.getFlaggedUploads() + 1);
            creator.setTrustHistoryScore(Math.max(0, creator.getTrustHistoryScore() - 5));
        } else if (eval.decision() == ValidationDecision.AUTO_APPROVED) {
            creator.setTrustHistoryScore(Math.min(100, creator.getTrustHistoryScore() + 1));
        }
        creatorRepository.save(creator);
    }
}

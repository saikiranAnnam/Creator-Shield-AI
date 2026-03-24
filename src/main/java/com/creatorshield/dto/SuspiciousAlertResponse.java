package com.creatorshield.dto;

import com.creatorshield.domain.SuspiciousAlert;
import java.time.Instant;

public record SuspiciousAlertResponse(
        long alertId,
        long uploadId,
        String creatorId,
        String alertType,
        String alertMessage,
        String status,
        Instant createdAt) {

    public static SuspiciousAlertResponse from(SuspiciousAlert a) {
        return new SuspiciousAlertResponse(
                a.getId(),
                a.getUpload().getId(),
                a.getUpload().getCreator().getExternalId(),
                a.getAlertType(),
                a.getAlertMessage(),
                a.getStatus().name(),
                a.getCreatedAt());
    }
}

package com.creatorshield.service;

import com.creatorshield.domain.AlertRecordStatus;
import com.creatorshield.domain.RiskLevel;
import com.creatorshield.dto.SuspiciousAlertResponse;
import com.creatorshield.repository.SuspiciousAlertRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuspiciousAlertQueryService {

    private final SuspiciousAlertRepository suspiciousAlertRepository;

    @Transactional(readOnly = true)
    public List<SuspiciousAlertResponse> listSuspicious(
            String creatorExternalId,
            Instant from,
            Instant to,
            RiskLevel riskLevel,
            AlertRecordStatus alertStatus) {
        return suspiciousAlertRepository
                .findSuspiciousWithFilters(creatorExternalId, from, to, riskLevel, alertStatus)
                .stream()
                .map(SuspiciousAlertResponse::from)
                .toList();
    }
}

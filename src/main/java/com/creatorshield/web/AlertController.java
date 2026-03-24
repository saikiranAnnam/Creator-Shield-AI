package com.creatorshield.web;

import com.creatorshield.domain.AlertRecordStatus;
import com.creatorshield.domain.RiskLevel;
import com.creatorshield.dto.SuspiciousAlertResponse;
import com.creatorshield.service.SuspiciousAlertQueryService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final SuspiciousAlertQueryService suspiciousAlertQueryService;

    @GetMapping("/suspicious")
    public List<SuspiciousAlertResponse> listSuspicious(
            @RequestParam(required = false) String creatorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Instant to,
            @RequestParam(required = false) RiskLevel riskLevel,
            @RequestParam(required = false) AlertRecordStatus alertStatus) {
        return suspiciousAlertQueryService.listSuspicious(creatorId, from, to, riskLevel, alertStatus);
    }
}

package com.creatorshield.repository;

import com.creatorshield.domain.AlertRecordStatus;
import com.creatorshield.domain.RiskLevel;
import com.creatorshield.domain.SuspiciousAlert;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SuspiciousAlertRepository extends JpaRepository<SuspiciousAlert, Long> {

    @Query(
            """
            SELECT a FROM SuspiciousAlert a
            JOIN FETCH a.upload u
            JOIN FETCH u.creator c
            WHERE (:creatorExternalId IS NULL OR c.externalId = :creatorExternalId)
              AND (:from IS NULL OR a.createdAt >= :from)
              AND (:to IS NULL OR a.createdAt <= :to)
              AND (:alertStatus IS NULL OR a.status = :alertStatus)
              AND (:riskLevel IS NULL OR EXISTS (
                    SELECT 1 FROM ValidationResult vr WHERE vr.upload.id = u.id AND vr.riskLevel = :riskLevel
                  ))
            ORDER BY a.createdAt DESC
            """)
    List<SuspiciousAlert> findSuspiciousWithFilters(
            @Param("creatorExternalId") String creatorExternalId,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("riskLevel") RiskLevel riskLevel,
            @Param("alertStatus") AlertRecordStatus alertStatus);
}

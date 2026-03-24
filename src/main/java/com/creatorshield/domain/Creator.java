package com.creatorshield.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "creators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, length = 255)
    private String externalId;

    @Column(name = "creator_name", nullable = false, length = 512)
    private String creatorName;

    @Column(name = "account_created_at", nullable = false)
    private Instant accountCreatedAt;

    @Column(name = "trust_history_score", nullable = false)
    private Integer trustHistoryScore;

    @Column(name = "total_uploads", nullable = false)
    private Integer totalUploads;

    @Column(name = "flagged_uploads", nullable = false)
    private Integer flaggedUploads;

    @Column(length = 64)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 32)
    private AccountStatus accountStatus;
}

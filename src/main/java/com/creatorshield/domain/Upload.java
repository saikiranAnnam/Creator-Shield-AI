package com.creatorshield.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "uploads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private Creator creator;

    @Column(name = "song_title", nullable = false, length = 512)
    private String songTitle;

    @Column(name = "artist_name", nullable = false, length = 512)
    private String artistName;

    @Column(name = "album_name", length = 512)
    private String albumName;

    @Column(name = "label_name", length = 512)
    private String labelName;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(length = 128)
    private String genre;

    @Column(length = 64)
    private String language;

    @Column(columnDefinition = "text")
    private String lyrics;

    @Column(name = "optional_external_claims", columnDefinition = "text")
    private String optionalExternalClaims;

    @Column(name = "normalized_title", nullable = false, length = 512)
    private String normalizedTitle;

    @Column(name = "normalized_artist", nullable = false, length = 512)
    private String normalizedArtist;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "upload_status", nullable = false, length = 32)
    private UploadStatus uploadStatus;
}

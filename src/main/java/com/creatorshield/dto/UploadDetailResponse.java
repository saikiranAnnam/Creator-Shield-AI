package com.creatorshield.dto;

import com.creatorshield.domain.Upload;
import java.time.Instant;
import java.time.LocalDate;

public record UploadDetailResponse(
        long uploadId,
        String creatorId,
        String songTitle,
        String artistName,
        String albumName,
        String labelName,
        LocalDate releaseDate,
        String genre,
        String language,
        String uploadStatus,
        Instant createdAt) {

    public static UploadDetailResponse from(Upload u) {
        return new UploadDetailResponse(
                u.getId(),
                u.getCreator().getExternalId(),
                u.getSongTitle(),
                u.getArtistName(),
                u.getAlbumName(),
                u.getLabelName(),
                u.getReleaseDate(),
                u.getGenre(),
                u.getLanguage(),
                u.getUploadStatus().name(),
                u.getCreatedAt());
    }
}

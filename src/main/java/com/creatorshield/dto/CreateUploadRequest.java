package com.creatorshield.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateUploadRequest(
        @NotBlank String creatorId,
        String creatorName,
        @NotBlank String songTitle,
        @NotBlank String artistName,
        String albumName,
        String labelName,
        LocalDate releaseDate,
        String genre,
        String language,
        String lyrics,
        String optionalExternalClaims,
        String country) {}

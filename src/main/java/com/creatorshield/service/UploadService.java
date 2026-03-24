package com.creatorshield.service;

import com.creatorshield.domain.AccountStatus;
import com.creatorshield.domain.Creator;
import com.creatorshield.domain.Upload;
import com.creatorshield.domain.UploadStatus;
import com.creatorshield.dto.CreateUploadRequest;
import com.creatorshield.dto.UploadCreatedResponse;
import com.creatorshield.dto.UploadDetailResponse;
import com.creatorshield.repository.CreatorRepository;
import com.creatorshield.repository.UploadRepository;
import com.creatorshield.util.MetadataNormalizer;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final CreatorRepository creatorRepository;
    private final UploadRepository uploadRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UploadCreatedResponse createUpload(CreateUploadRequest request) {
        Creator creator = resolveCreator(request);
        creator.setTotalUploads(creator.getTotalUploads() + 1);
        creatorRepository.save(creator);

        Instant now = Instant.now();
        Upload upload =
                Upload.builder()
                        .creator(creator)
                        .songTitle(request.songTitle().trim())
                        .artistName(request.artistName().trim())
                        .albumName(trimToNull(request.albumName()))
                        .labelName(trimToNull(request.labelName()))
                        .releaseDate(request.releaseDate())
                        .genre(trimToNull(request.genre()))
                        .language(trimToNull(request.language()))
                        .lyrics(request.lyrics())
                        .optionalExternalClaims(request.optionalExternalClaims())
                        .normalizedTitle(MetadataNormalizer.normalize(request.songTitle()))
                        .normalizedArtist(MetadataNormalizer.normalize(request.artistName()))
                        .createdAt(now)
                        .uploadStatus(UploadStatus.PENDING_VALIDATION)
                        .build();
        upload = uploadRepository.save(upload);

        eventPublisher.publishEvent(new UploadPendingEvent(upload.getId()));

        return new UploadCreatedResponse(
                upload.getId(), upload.getUploadStatus().name(), upload.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public UploadDetailResponse getUpload(long id) {
        Upload upload =
                uploadRepository.findByIdWithCreator(id).orElseThrow(() -> new NotFoundException("Upload not found"));
        return UploadDetailResponse.from(upload);
    }

    private Creator resolveCreator(CreateUploadRequest request) {
        return creatorRepository
                .findByExternalId(request.creatorId())
                .orElseGet(
                        () -> {
                            if (request.creatorName() == null || request.creatorName().isBlank()) {
                                throw new IllegalArgumentException(
                                        "creatorName is required when creatorId is unknown");
                            }
                            return Creator.builder()
                                    .externalId(request.creatorId())
                                    .creatorName(request.creatorName().trim())
                                    .accountCreatedAt(Instant.now())
                                    .trustHistoryScore(50)
                                    .totalUploads(0)
                                    .flaggedUploads(0)
                                    .country(request.country())
                                    .accountStatus(AccountStatus.ACTIVE)
                                    .build();
                        });
    }

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

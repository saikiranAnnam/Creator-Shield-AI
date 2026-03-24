package com.creatorshield.validation;

import com.creatorshield.domain.Upload;
import com.creatorshield.repository.UploadRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DuplicateCheckService {

    private static final int RECENT_UPLOAD_WINDOW_DAYS = 90;
    private static final int MAX_CANDIDATES = 800;

    private final UploadRepository uploadRepository;
    private final JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();

    public DuplicateSignals analyze(Upload upload) {
        boolean exactDuplicateOtherCreator = hasExactDuplicateUnderAnotherCreator(upload);
        double duplicateScore = exactDuplicateOtherCreator ? 1.0 : 0.0;

        Instant since = Instant.now().minus(RECENT_UPLOAD_WINDOW_DAYS, ChronoUnit.DAYS);
        var recent = uploadRepository.findByIdNotAndCreatedAtAfter(
                upload.getId(), since, PageRequest.of(0, MAX_CANDIDATES));

        double maxTitleSim =
                recent.stream()
                        .map(u -> jaroWinkler.apply(upload.getNormalizedTitle(), u.getNormalizedTitle()))
                        .max(Comparator.naturalOrder())
                        .orElse(0.0);

        return new DuplicateSignals(duplicateScore, maxTitleSim, exactDuplicateOtherCreator);
    }

    private boolean hasExactDuplicateUnderAnotherCreator(Upload upload) {
        var candidates =
                uploadRepository.findByNormalizedTitleAndNormalizedArtistAndCreatorIdNotAndIdNot(
                        upload.getNormalizedTitle(),
                        upload.getNormalizedArtist(),
                        upload.getCreator().getId(),
                        upload.getId());
        return candidates.stream()
                .anyMatch(u -> Objects.equals(u.getReleaseDate(), upload.getReleaseDate()));
    }
}

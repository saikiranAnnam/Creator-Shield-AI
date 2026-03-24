package com.creatorshield.repository;

import com.creatorshield.domain.Upload;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UploadRepository extends JpaRepository<Upload, Long> {

    long countByCreator_IdAndCreatedAtAfter(Long creatorId, Instant since);

    @Query("SELECT u FROM Upload u JOIN FETCH u.creator WHERE u.id = :id")
    Optional<Upload> findByIdWithCreator(@Param("id") Long id);

    List<Upload> findByNormalizedTitleAndNormalizedArtistAndCreatorIdNotAndIdNot(
            String normalizedTitle, String normalizedArtist, Long creatorId, Long excludeUploadId);

    List<Upload> findByIdNotAndCreatedAtAfter(Long id, Instant since, Pageable pageable);
}

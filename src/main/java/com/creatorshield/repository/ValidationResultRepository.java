package com.creatorshield.repository;

import com.creatorshield.domain.ValidationResult;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ValidationResultRepository extends JpaRepository<ValidationResult, Long> {

    @Query("SELECT vr FROM ValidationResult vr JOIN FETCH vr.upload WHERE vr.upload.id = :uploadId")
    Optional<ValidationResult> findFetchedByUploadId(@Param("uploadId") Long uploadId);

    Optional<ValidationResult> findByUpload_Id(Long uploadId);
}

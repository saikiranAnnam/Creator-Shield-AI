package com.creatorshield.service;

import com.creatorshield.domain.ValidationResult;
import com.creatorshield.dto.ValidationResultResponse;
import com.creatorshield.repository.ValidationResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ValidationQueryService {

    private final ValidationResultRepository validationResultRepository;

    @Transactional(readOnly = true)
    public ValidationResultResponse getByUploadId(long uploadId) {
        ValidationResult vr =
                validationResultRepository
                        .findFetchedByUploadId(uploadId)
                        .orElseThrow(() -> new NotFoundException("Validation result not found"));
        return ValidationResultResponse.from(vr);
    }
}

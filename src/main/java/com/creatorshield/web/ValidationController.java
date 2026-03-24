package com.creatorshield.web;

import com.creatorshield.dto.ValidationResultResponse;
import com.creatorshield.service.ValidationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validations")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationQueryService validationQueryService;

    @GetMapping("/{uploadId}")
    public ValidationResultResponse get(@PathVariable long uploadId) {
        return validationQueryService.getByUploadId(uploadId);
    }
}

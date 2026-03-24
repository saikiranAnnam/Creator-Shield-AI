package com.creatorshield.web;

import com.creatorshield.dto.CreateUploadRequest;
import com.creatorshield.dto.UploadCreatedResponse;
import com.creatorshield.dto.UploadDetailResponse;
import com.creatorshield.service.UploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadCreatedResponse create(@Valid @RequestBody CreateUploadRequest request) {
        return uploadService.createUpload(request);
    }

    @GetMapping("/{uploadId}")
    public UploadDetailResponse get(@PathVariable long uploadId) {
        return uploadService.getUpload(uploadId);
    }
}

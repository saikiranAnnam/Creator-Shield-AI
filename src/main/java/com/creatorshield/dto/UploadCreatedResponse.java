package com.creatorshield.dto;

import java.time.Instant;

public record UploadCreatedResponse(long uploadId, String status, Instant createdAt) {}

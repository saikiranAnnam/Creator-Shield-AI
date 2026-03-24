package com.creatorshield.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UploadCreatedEvent(
        long uploadId,
        long creatorInternalId,
        String creatorExternalId,
        Instant timestamp,
        Map<String, String> metadataSummary) {}

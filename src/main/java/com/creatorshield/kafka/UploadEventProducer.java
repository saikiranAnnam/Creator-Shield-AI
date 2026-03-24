package com.creatorshield.kafka;

import com.creatorshield.domain.Upload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${creatorshield.kafka.topic.upload-created}")
    private String topic;

    public void publishUploadCreated(Upload upload) {
        var event =
                new UploadCreatedEvent(
                        upload.getId(),
                        upload.getCreator().getId(),
                        upload.getCreator().getExternalId(),
                        Instant.now(),
                        Map.of(
                                "songTitle", truncate(upload.getSongTitle(), 200),
                                "artistName", truncate(upload.getArtistName(), 200)));
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, String.valueOf(upload.getId()), json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize upload event", e);
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max);
    }
}

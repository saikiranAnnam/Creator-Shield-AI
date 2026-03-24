package com.creatorshield.kafka;

import com.creatorshield.service.UploadValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadValidationConsumer {

    private final UploadValidationService uploadValidationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${creatorshield.kafka.topic.upload-created}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(
            @Payload String payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            UploadCreatedEvent event = objectMapper.readValue(payload, UploadCreatedEvent.class);
            uploadValidationService.validateUpload(event.uploadId());
        } catch (JsonProcessingException e) {
            log.error("Invalid Kafka payload on topic={}", topic, e);
            throw new IllegalStateException("Invalid upload event JSON", e);
        } catch (Exception e) {
            log.error("Failed to process upload validation from topic={}", topic, e);
            throw e;
        }
    }
}

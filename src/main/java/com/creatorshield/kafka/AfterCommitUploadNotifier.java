package com.creatorshield.kafka;

import com.creatorshield.repository.UploadRepository;
import com.creatorshield.service.UploadPendingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AfterCommitUploadNotifier {

    private final UploadRepository uploadRepository;
    private final UploadEventProducer uploadEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishAfterCommit(UploadPendingEvent event) {
        uploadRepository
                .findByIdWithCreator(event.uploadId())
                .ifPresent(uploadEventProducer::publishUploadCreated);
    }
}

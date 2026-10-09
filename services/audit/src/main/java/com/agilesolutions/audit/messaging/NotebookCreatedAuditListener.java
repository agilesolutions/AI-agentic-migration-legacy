package com.agilesolutions.audit.messaging;

import com.agilesolutions.audit.persistence.NotebookAudit;
import com.agilesolutions.audit.persistence.NotebookAuditRepository;
import com.agilesolutions.common.events.NotebookCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotebookCreatedAuditListener {

    private final NotebookAuditRepository repository;

    @KafkaListener(
            topics = "${app.kafka.topics.notebook-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handle(NotebookCreated event) {

        log.info(
                "AUDIT: NotebookCreated event received: eventId={}, notebookId={}",
                event.getEventId(),
                event.getNotebookId()
        );

        NotebookAudit audit = new NotebookAudit(
                event.getEventId(),
                event.getEventType(),
                event.getEventVersion(),
                event.getNotebookId(),
                event.getTitle(),
                event.getDescription(),
                Instant.parse(event.getOccurredAt()),
                Instant.now()
        );

        repository.save(audit);

        log.info(
                "AUDIT: NotebookCreated persisted: eventId={}, notebookId={}",
                event.getEventId(),
                event.getNotebookId()
        );
    }
}
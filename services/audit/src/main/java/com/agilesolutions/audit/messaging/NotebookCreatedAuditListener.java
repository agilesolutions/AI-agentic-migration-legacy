package com.agilesolutions.audit.messaging;

import com.agilesolutions.audit.persistence.NotebookAudit;
import com.agilesolutions.audit.persistence.NotebookAuditRepository;
import com.agilesolutions.common.events.NotebookCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NotebookCreatedAuditListener {

    private static final Logger log =
            LoggerFactory.getLogger(NotebookCreatedAuditListener.class);

    private final NotebookAuditRepository repository;

    public NotebookCreatedAuditListener(
            NotebookAuditRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.notebook-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handle(NotebookCreated event) {

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
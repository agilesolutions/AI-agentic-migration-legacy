package com.agilesolutions.notification.messaging;

import com.agilesolutions.common.events.NotebookCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotebookCreatedListener {

    @KafkaListener(
            topics = "${app.kafka.topics.notebook-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handle(NotebookCreated event) {

        log.info(
                "NOTIFICATION: Notebook created: eventId={}, notebookId={}, title={}, occurredAt={}",
                event.getEventId(),
                event.getNotebookId(),
                event.getTitle(),
                event.getOccurredAt()
        );
    }

}
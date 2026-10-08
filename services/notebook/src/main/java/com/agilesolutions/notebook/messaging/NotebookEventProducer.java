package com.agilesolutions.notebook.messaging;

import com.agilesolutions.common.events.NotebookCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class NotebookEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String notebookEventsTopic;

    public NotebookEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.notebook-events}")
            String notebookEventsTopic) {

        this.kafkaTemplate = kafkaTemplate;
        this.notebookEventsTopic = notebookEventsTopic;
    }

    public CompletableFuture<SendResult<String, Object>>
    publishNotebookCreated(
            UUID notebookId,
            String title,
            String description) {

        log.info("Publishing NotebookCreated event for notebookId: {}", notebookId);

        NotebookCreated event = NotebookCreated.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType("NotebookCreated")
                .setEventVersion(1)
                .setOccurredAt(Instant.now().toString())
                .setNotebookId(notebookId.toString())
                .setTitle(title)
                .setDescription(description)
                .build();

        return kafkaTemplate.send(
                notebookEventsTopic,
                notebookId.toString(),
                event
        );
    }
}
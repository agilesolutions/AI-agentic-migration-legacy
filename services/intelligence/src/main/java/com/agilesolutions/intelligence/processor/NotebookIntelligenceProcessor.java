
package com.agilesolutions.intelligence.processor;

import com.agilesolutions.intelligence.document.NotebookIntelligenceDocument;
import com.agilesolutions.intelligence.document.NotebookIntelligenceDocument.SourceEvent;
import com.agilesolutions.intelligence.enrichment.NotebookEnrichmentService;
import com.agilesolutions.intelligence.repository.NotebookIntelligenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotebookIntelligenceProcessor {

    private final NotebookIntelligenceRepository repository;
    private final NotebookEnrichmentService enrichmentService;

    public void process(
            ConsumerRecord<String, GenericRecord> record) {

        log.info("Processing notebook event: topic={}, partition={}, offset={}", record.topic(), record.partition(), record.offset());

        GenericRecord event = record.value();

        if (event == null) {
            throw new IllegalArgumentException(
                    "Notebook event value must not be null");
        }

        String notebookId = requiredString(event, "notebookId");
        String title = requiredString(event, "title");
        String description = optionalString(event, "description");

        // Validate the ID early so malformed events can be routed
        // through the Kafka error-handling policy.
        UUID.fromString(notebookId);

        Instant now = Instant.now();

        NotebookIntelligenceDocument document =
                repository.findById(notebookId)
                        .orElseGet(NotebookIntelligenceDocument::new);

        boolean isNew = document.getCreatedAt() == null;

        document.setNotebookId(notebookId);
        document.setTitle(title);
        document.setDescription(description);
        document.setUpdatedAt(now);

        if (isNew) {
            document.setCreatedAt(now);
        }

        SourceEvent source = new SourceEvent();
        source.setEventId(optionalString(event, "eventId"));
        source.setEventType("NotebookCreated");
        source.setTopic(record.topic());
        source.setPartition(record.partition());
        source.setOffset(record.offset());
        source.setSchemaVersion(readSchemaVersion(event));

        document.setSource(source);

        log.info("Enriching notebook intelligence document for notebookId={}", notebookId);

        enrichmentService.enrich(document);
        document.setEnrichedAt(Instant.now());

        log.info("Saving notebook intelligence document for notebookId={}", notebookId);

        repository.save(document);
    }

    private String requiredString(
            GenericRecord record,
            String field) {

        String value = optionalString(record, field);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Required Avro field is missing: " + field);
        }

        return value;
    }

    private String optionalString(
            GenericRecord record,
            String field) {

        Object value = record.get(field);

        return value == null ? null : value.toString();
    }

    private int readSchemaVersion(GenericRecord record) {
        String version = optionalString(record, "schemaVersion");

        if (version == null) {
            return 1;
        }

        try {
            return Integer.parseInt(version);
        } catch (NumberFormatException exception) {
            return 1;
        }
    }
}
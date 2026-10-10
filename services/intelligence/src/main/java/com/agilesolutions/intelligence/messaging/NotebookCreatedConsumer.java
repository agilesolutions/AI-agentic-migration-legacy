package com.agilesolutions.intelligence.messaging;

import com.agilesolutions.intelligence.processor.NotebookIntelligenceProcessor;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotebookCreatedConsumer {

    private final NotebookIntelligenceProcessor processor;

    public NotebookCreatedConsumer(
            NotebookIntelligenceProcessor processor) {
        this.processor = processor;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.notebook-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(
            ConsumerRecord<String, GenericRecord> record) {

        processor.process(
                record.value().get("notebookId").toString(),
                record.value().get("title").toString(),
                record.value().get("description").toString()
        );
    }
}
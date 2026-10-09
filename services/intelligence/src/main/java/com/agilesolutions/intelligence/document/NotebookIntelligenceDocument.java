
package com.agilesolutions.intelligence.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "notebook_intelligence")
@CompoundIndex(
        name = "status_updated_idx",
        def = "{'status': 1, 'updatedAt': -1}"
)
@Data
@NoArgsConstructor
public class NotebookIntelligenceDocument {

    @Id
    private String notebookId;

    private String title;
    private String description;

    @Indexed
    private String status = "PENDING";

    private List<String> topics = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private List<ExtractedEntity> entities = new ArrayList<>();

    private String summary;

    private SourceEvent source;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant enrichedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractedEntity {
        private String name;
        private String type;


    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceEvent {
        private String eventId;
        private String eventType;
        private String topic;
        private int partition;
        private long offset;
        private int schemaVersion;

    }
}
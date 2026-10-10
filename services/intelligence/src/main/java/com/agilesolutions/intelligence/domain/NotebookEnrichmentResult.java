
package com.agilesolutions.intelligence.domain;

import java.util.List;

public record NotebookEnrichmentResult(
        String summary,
        List<String> topics,
        List<String> keywords,
        List<ExtractedEntity> entities
) {
    public NotebookEnrichmentResult {
        topics = topics == null ? List.of() : List.copyOf(topics);
        keywords = keywords == null ? List.of() : List.copyOf(keywords);
        entities = entities == null ? List.of() : List.copyOf(entities);
        summary = summary == null ? "" : summary.trim();
    }

    public record ExtractedEntity(
            String name,
            String type
    ) {}
}
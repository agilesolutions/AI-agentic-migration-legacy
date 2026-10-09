
package com.agilesolutions.intelligence.enrichment;

import com.agilesolutions.intelligence.document.NotebookIntelligenceDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class NotebookEnrichmentService {

    private static final Map<String, List<String>> TOPIC_TERMS =
            Map.of(
                    "JAVA", List.of("java", "spring boot", "jvm"),
                    "CLOUD NATIVE", List.of(
                            "kubernetes", "docker", "openshift",
                            "cloud native", "microservices"
                    ),
                    "MESSAGING", List.of(
                            "kafka", "avro", "redpanda", "messaging"
                    ),
                    "DATABASES", List.of(
                            "mongodb", "mongo", "postgresql", "database"
                    ),
                    "AI", List.of(
                            "ai", "artificial intelligence",
                            "llm", "machine learning", "embeddings"
                    ),
                    "OBSERVABILITY", List.of(
                            "grafana", "prometheus", "opentelemetry",
                            "tracing", "metrics"
                    )
            );

    public void enrich(NotebookIntelligenceDocument document) {
        String text = (
                nullToEmpty(document.getTitle()) + " " +
                        nullToEmpty(document.getDescription())
        ).toLowerCase(Locale.ROOT);

        Set<String> topics = new LinkedHashSet<>();
        Set<String> keywords = new LinkedHashSet<>();

        TOPIC_TERMS.forEach((topic, terms) -> {
            for (String term : terms) {
                if (text.contains(term)) {
                    topics.add(topic);
                    keywords.add(term);
                }
            }
        });

        document.setTopics(new ArrayList<>(topics));
        document.setKeywords(new ArrayList<>(keywords));
        document.setSummary(createSummary(document));
        document.setStatus("COMPLETED");
    }

    private String createSummary(
            NotebookIntelligenceDocument document) {

        String title = nullToEmpty(document.getTitle()).trim();
        String description =
                nullToEmpty(document.getDescription()).trim();

        if (!description.isBlank()) {
            return description.length() <= 300
                    ? description
                    : description.substring(0, 297) + "...";
        }

        if (!title.isBlank()) {
            return "Notebook about " + title + ".";
        }

        return "Notebook awaiting further enrichment.";
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
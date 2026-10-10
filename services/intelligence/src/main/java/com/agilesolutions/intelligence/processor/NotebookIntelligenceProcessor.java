
package com.agilesolutions.intelligence.processor;

import com.agilesolutions.intelligence.domain.NotebookEnrichmentResult;
import com.agilesolutions.intelligence.document.NotebookIntelligenceDocument;
import com.agilesolutions.intelligence.repository.NotebookIntelligenceRepository;
import com.agilesolutions.intelligence.service.NotebookAiEnrichmentService;
import org.springframework.stereotype.Service;

@Service
public class NotebookIntelligenceProcessor {

    private final NotebookAiEnrichmentService aiEnrichmentService;
    private final NotebookIntelligenceRepository repository;

    public NotebookIntelligenceProcessor(
            NotebookAiEnrichmentService aiEnrichmentService,
            NotebookIntelligenceRepository repository) {
        this.aiEnrichmentService = aiEnrichmentService;
        this.repository = repository;
    }

    public void process(
            String notebookId,
            String title,
            String description) {

        // Generate a genuine AI summary, topics, keywords and entities.
        NotebookEnrichmentResult enrichment =
                aiEnrichmentService.enrich(title, description);

        // Preserve the source notebook ID; the LLM must not generate it.
        NotebookIntelligenceDocument document =
                new NotebookIntelligenceDocument();

        document.setNotebookId(notebookId);
        document.setTitle(title);
        document.setDescription(description);
        document.setSummary(enrichment.summary());
        document.setTopics(enrichment.topics());
        document.setKeywords(enrichment.keywords());

        document.setEntities(
                enrichment.entities().stream()
                        .map(entity -> {
                            var extracted = new NotebookIntelligenceDocument
                                    .ExtractedEntity();
                            extracted.setName(entity.name());
                            extracted.setType(entity.type());
                            return extracted;
                        })
                        .toList());

        document.setStatus("COMPLETED");

        repository.save(document);
    }
}
package com.agilesolutions.intelligence.processor;

import com.agilesolutions.intelligence.document.NotebookIntelligenceDocument;
import com.agilesolutions.intelligence.domain.NotebookEnrichmentResult;
import com.agilesolutions.intelligence.repository.NotebookIntelligenceRepository;
import com.agilesolutions.intelligence.service.NotebookAiEnrichmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotebookIntelligenceProcessorTest {

    @Mock
    private NotebookAiEnrichmentService aiEnrichmentService;

    @Mock
    private NotebookIntelligenceRepository repository;

    @InjectMocks
    private NotebookIntelligenceProcessor processor;

    @Test
    void process_mapsEnrichmentAndSavesDocument() {
        var enrichment = new NotebookEnrichmentResult(
                "A concise summary",
                List.of("Engineering", "AI"),
                List.of("notebook", "summary"),
                List.of(
                        new NotebookEnrichmentResult.ExtractedEntity("Java", "TECHNOLOGY"),
                        new NotebookEnrichmentResult.ExtractedEntity("OpenAI", "ORGANIZATION")
                )
        );

        when(aiEnrichmentService.enrich("Planning session", "Project kickoff notes"))
                .thenReturn(enrichment);

        processor.process("nb-123", "Planning session", "Project kickoff notes");

        ArgumentCaptor<NotebookIntelligenceDocument> documentCaptor = ArgumentCaptor.forClass(NotebookIntelligenceDocument.class);
        verify(aiEnrichmentService).enrich("Planning session", "Project kickoff notes");
        verify(repository).save(documentCaptor.capture());

        NotebookIntelligenceDocument saved = documentCaptor.getValue();

        assertThat(saved.getNotebookId()).isEqualTo("nb-123");
        assertThat(saved.getTitle()).isEqualTo("Planning session");
        assertThat(saved.getDescription()).isEqualTo("Project kickoff notes");
        assertThat(saved.getSummary()).isEqualTo("A concise summary");
        assertThat(saved.getTopics()).containsExactly("Engineering", "AI");
        assertThat(saved.getKeywords()).containsExactly("notebook", "summary");
        assertThat(saved.getStatus()).isEqualTo("COMPLETED");
        assertThat(saved.getEntities()).hasSize(2);
        assertThat(saved.getEntities().getFirst().getName()).isEqualTo("Java");
        assertThat(saved.getEntities().getFirst().getType()).isEqualTo("TECHNOLOGY");
        assertThat(saved.getEntities().get(1).getName()).isEqualTo("OpenAI");
        assertThat(saved.getEntities().get(1).getType()).isEqualTo("ORGANIZATION");
    }
}

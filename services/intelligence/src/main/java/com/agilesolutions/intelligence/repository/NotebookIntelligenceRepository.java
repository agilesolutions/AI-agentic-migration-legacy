package com.agilesolutions.intelligence.repository;

import com.agilesolutions.intelligence.document.NotebookIntelligenceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotebookIntelligenceRepository
        extends MongoRepository<NotebookIntelligenceDocument, String> {

    @Query("{ 'topics': { '$regex': ?0, '$options': 'i' } }")
    List<NotebookIntelligenceDocument> findByTopic(String topic);

    List<NotebookIntelligenceDocument>
    findByTitleContainingIgnoreCase(String title);

    List<NotebookIntelligenceDocument>
    findByTopicsContainingIgnoreCase(String topic);

    List<NotebookIntelligenceDocument>
    findByStatus(String status);

    Optional<NotebookIntelligenceDocument>
    findBySource_EventId(String eventId);
}
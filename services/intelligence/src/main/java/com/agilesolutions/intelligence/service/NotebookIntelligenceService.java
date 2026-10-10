
package com.agilesolutions.intelligence.service;

import com.agilesolutions.common.exception.NotebookIntelligenceNotFoundException;
import com.agilesolutions.intelligence.document.NotebookIntelligenceDocument;
import com.agilesolutions.intelligence.repository.NotebookIntelligenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotebookIntelligenceService {

    private final NotebookIntelligenceRepository repository;

    @Transactional(readOnly = true)
    public NotebookIntelligenceDocument getById(String id) {

        log.info("Fetching Notebook Intelligence Document with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(
                        () -> new NotebookIntelligenceNotFoundException(id)
                );
    }

    @Transactional(readOnly = true)
    public List<NotebookIntelligenceDocument> search(String query) {

        log.info("Searching Notebook Intelligence Documents with query: {}", query);

        if (query == null || query.isBlank()) {
            return repository.findAll();
        }

        return repository.findByTitleContainingIgnoreCase(query.trim());
    }

    @Transactional(readOnly = true)
    public List<NotebookIntelligenceDocument> getByTopic(String topic) {

        log.info("Fetching Notebook Intelligence Documents with topic: {}", topic);

        return repository.findByTopic(
                java.util.regex.Pattern.quote(topic)
        );
    }
}
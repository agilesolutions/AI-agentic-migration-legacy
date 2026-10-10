package com.agilesolutions.notebook.service;

import com.agilesolutions.common.exception.NotebookNotFoundException;
import com.agilesolutions.notebook.api.model.CreateNotebookRequest;
import com.agilesolutions.notebook.api.model.Notebook;
import com.agilesolutions.notebook.api.model.UpdateNotebookRequest;
import com.agilesolutions.notebook.entity.NotebookEntity;
import com.agilesolutions.notebook.messaging.NotebookEventProducer;
import com.agilesolutions.notebook.repository.NotebookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotebookService {

    private final NotebookRepository repository;
    private final NotebookEventProducer eventProducer;


    @Transactional(readOnly = true)
    public List<Notebook> getAllNotebooks() {

        return repository.findAll()
                .stream()
                .map(this::toApiModel)
                .toList();
    }


    @Transactional(readOnly = true)
    public Notebook getNotebookById(UUID id) {

        NotebookEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new NotebookNotFoundException(id));

        return toApiModel(entity);
    }


    @Transactional
    public Notebook createNotebook(
            CreateNotebookRequest request) {

        NotebookEntity entity = new NotebookEntity();

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());

        log.info(
                "Creating notebook with title: {} and description: {}",
                request.getTitle(),
                request.getDescription()
        );

        NotebookEntity saved =
                repository.save(entity);

        log.info(
                "Notebook created with ID: {}, title: {}, description: {}",
                saved.getId(),
                saved.getTitle(),
                saved.getDescription()
        );

        log.info(
                "Publishing NotebookCreated event for notebook ID: {}",
                saved.getId()
        );

        eventProducer
                .publishNotebookCreated(
                        saved.getId(),
                        saved.getTitle(),
                        saved.getDescription()
                )
                .whenComplete((result, exception) -> {

                    if (exception != null) {
                        log.error(
                                "Failed to publish NotebookCreated event for notebook {}",
                                saved.getId(),
                                exception
                        );
                        return;
                    }

                    log.info(
                            "Published NotebookCreated: notebookId={}, topic={}, partition={}, offset={}",
                            saved.getId(),
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset()
                    );
                });

        return toApiModel(saved);
    }


    public Notebook updateNotebook(
            UUID id,
            UpdateNotebookRequest request) {

        NotebookEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new NotebookNotFoundException(id));

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());

        NotebookEntity saved =
                repository.save(entity);

        return toApiModel(saved);
    }


    public void deleteNotebook(UUID id) {

        if (!repository.existsById(id)) {
            throw new NotebookNotFoundException(id);
        }

        repository.deleteById(id);
    }


    private Notebook toApiModel(
            NotebookEntity entity) {

        Notebook notebook = new Notebook();

        notebook.setId(entity.getId());
        notebook.setTitle(entity.getTitle());
        notebook.setDescription(entity.getDescription());
        notebook.setCreatedAt(entity.getCreatedAt());
        notebook.setUpdatedAt(entity.getUpdatedAt());

        return notebook;
    }
}
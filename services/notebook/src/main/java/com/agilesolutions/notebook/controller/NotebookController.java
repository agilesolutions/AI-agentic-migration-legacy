package com.agilesolutions.notebook.controller;

import com.agilesolutions.notebook.api.NotebooksApi;
import com.agilesolutions.notebook.api.model.CreateNotebookRequest;
import com.agilesolutions.notebook.api.model.Notebook;
import com.agilesolutions.notebook.api.model.UpdateNotebookRequest;
import com.agilesolutions.notebook.service.NotebookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class NotebookController
        implements NotebooksApi {

    private final NotebookService notebookService;


    public NotebookController(
            NotebookService notebookService) {

        this.notebookService = notebookService;
    }


    @Override
    public ResponseEntity<List<Notebook>> getAllNotebooks() {

        return ResponseEntity.ok(
                notebookService.getAllNotebooks()
        );
    }


    @Override
    public ResponseEntity<Notebook>
    createNotebook(
            CreateNotebookRequest request) {

        Notebook notebook =
                notebookService.createNotebook(request);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/v1/notebooks/"
                                        + notebook.getId()
                        )
                )
                .body(notebook);
    }


    @Override
    public ResponseEntity<Notebook>
    getNotebookById(UUID id) {

        return ResponseEntity.ok(
                notebookService.getNotebookById(id)
        );
    }


    @Override
    public ResponseEntity<Notebook>
    updateNotebook(
            UUID id,
            UpdateNotebookRequest request) {

        return ResponseEntity.ok(
                notebookService.updateNotebook(
                        id,
                        request
                )
        );
    }


    @Override
    public ResponseEntity<Void>
    deleteNotebook(UUID id) {

        notebookService.deleteNotebook(id);

        return ResponseEntity.noContent().build();
    }
}
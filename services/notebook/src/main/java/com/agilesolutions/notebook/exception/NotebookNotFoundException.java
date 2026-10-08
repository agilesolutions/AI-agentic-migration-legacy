package com.agilesolutions.notebook.exception;

import java.util.UUID;

public class NotebookNotFoundException extends RuntimeException {

    public NotebookNotFoundException(UUID id) {
        super("Notebook not found: " + id);
    }
}
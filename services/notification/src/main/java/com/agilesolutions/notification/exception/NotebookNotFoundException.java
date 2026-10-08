package com.agilesolutions.notification.exception;

import java.util.UUID;

public class NotebookNotFoundException extends RuntimeException {

    public NotebookNotFoundException(UUID id) {
        super("Notebook not found: " + id);
    }
}
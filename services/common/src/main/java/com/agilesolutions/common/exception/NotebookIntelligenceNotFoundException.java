package com.agilesolutions.common.exception;

public class NotebookIntelligenceNotFoundException
        extends RuntimeException {

    public NotebookIntelligenceNotFoundException(String id) {
        super("Notebook intelligence document not found: " + id);
    }
}
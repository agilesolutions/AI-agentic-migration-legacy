package com.agilesolutions.intelligence.persistence;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("notebook_audit")
@Getter
public class NotebookAudit {

    @Id
    private Long id;

    private String eventId;
    private String eventType;
    private Integer eventVersion;
    private String notebookId;
    private String title;
    private String description;
    private Instant occurredAt;
    private Instant auditedAt;

    protected NotebookAudit() {
    }

    public NotebookAudit(
            String eventId,
            String eventType,
            Integer eventVersion,
            String notebookId,
            String title,
            String description,
            Instant occurredAt,
            Instant auditedAt) {

        this.eventId = eventId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.notebookId = notebookId;
        this.title = title;
        this.description = description;
        this.occurredAt = occurredAt;
        this.auditedAt = auditedAt;
    }

}
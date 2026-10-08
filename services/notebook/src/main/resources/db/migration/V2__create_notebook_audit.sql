CREATE TABLE notebook_audit (
                                id              BIGSERIAL PRIMARY KEY,
                                event_id        VARCHAR(100) NOT NULL,
                                event_type      VARCHAR(100) NOT NULL,
                                event_version   INTEGER NOT NULL,
                                notebook_id     VARCHAR(100) NOT NULL,
                                title           VARCHAR(500) NOT NULL,
                                description     TEXT,
                                occurred_at     TIMESTAMP WITH TIME ZONE NOT NULL,
                                audited_at      TIMESTAMP WITH TIME ZONE NOT NULL,

                                CONSTRAINT uk_notebook_audit_event_id
                                    UNIQUE (event_id)
);

CREATE INDEX idx_notebook_audit_notebook_id
    ON notebook_audit (notebook_id);

CREATE INDEX idx_notebook_audit_occurred_at
    ON notebook_audit (occurred_at);
package com.agilesolutions.intelligence.persistence;

import org.springframework.data.repository.CrudRepository;

public interface NotebookAuditRepository
        extends CrudRepository<NotebookAudit, Long> {

}
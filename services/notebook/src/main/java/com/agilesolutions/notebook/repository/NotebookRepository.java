package com.agilesolutions.notebook.repository;

import com.agilesolutions.notebook.entity.NotebookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotebookRepository extends JpaRepository<NotebookEntity, UUID> {
}
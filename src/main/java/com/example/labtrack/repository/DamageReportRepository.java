package com.example.labtrack.repository;

import com.example.labtrack.domain.DamageReport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing DamageReport entities.
 * Provides standard CRUD operations via JpaRepository.
 */
public interface DamageReportRepository extends JpaRepository<DamageReport, Long> {}

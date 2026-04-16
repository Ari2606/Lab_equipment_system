package com.example.labtrack.service;

import com.example.labtrack.domain.DamageReport;
import com.example.labtrack.domain.User;
import com.example.labtrack.dto.DamageReportForm;

import java.util.List;

/**
 * Service interface defining business logic operations
 * related to damage reporting and verification.
 */
public interface DamageService {

    /**
     * Creates and stores a new damage report.
     *
     * @param form the form containing damage details
     * @param reportedBy the user who reported the damage
     * @return the created DamageReport entity
     */
    DamageReport reportDamage(DamageReportForm form, User reportedBy);

    /**
     * Verifies a damage report and optionally marks it as misuse.
     *
     * @param damageId the ID of the damage report to verify
     * @param misuse indicates whether the damage was due to misuse
     * @param verifier the user performing the verification
     * @return the updated DamageReport entity
     */
    DamageReport verify(Long damageId, boolean misuse, User verifier);

    /**
     * Retrieves all damage reports.
     *
     * @return list of all DamageReport entities
     */
    List<DamageReport> findAll();
}

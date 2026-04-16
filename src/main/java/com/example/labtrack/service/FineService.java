package com.example.labtrack.service;

import com.example.labtrack.domain.Fine;
import com.example.labtrack.domain.User;

import java.util.List;

/**
 * Service interface defining business logic operations
 * related to fines management.
 */
public interface FineService {

    /**
     * Creates a fine for late return or delay.
     *
     * @param student the student being fined
     * @param reason the reason for the fine
     * @return the created Fine entity
     */
    Fine createLateFine(User student, String reason);

    /**
     * Creates a fine associated with a damage report.
     *
     * @param student the student being fined
     * @param damageReportId the ID of the related damage report
     * @param reason the reason for the fine
     * @return the created Fine entity
     */
    Fine createDamageFine(User student, Long damageReportId, String reason);

    /**
     * Retrieves all fines for a specific student.
     *
     * @param student the student whose fines are to be fetched
     * @return list of fines associated with the student
     */
    List<Fine> studentFines(User student);

    /**
     * Retrieves all fines in the system.
     *
     * @return list of all Fine entities
     */
    List<Fine> allFines();

    /**
     * Marks a fine as paid.
     *
     * @param fineId the ID of the fine to be updated
     */
    void markPaid(Long fineId);
}

package com.example.labtrack.repository;

import com.example.labtrack.domain.Fine;
import com.example.labtrack.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Fine entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface FineRepository extends JpaRepository<Fine, Long> {

    /**
     * Retrieves all fines associated with a specific student.
     *
     * @param student the user (student) whose fines are to be fetched
     * @return list of fines belonging to the given student
     */
    List<Fine> findByStudent(User student);
}

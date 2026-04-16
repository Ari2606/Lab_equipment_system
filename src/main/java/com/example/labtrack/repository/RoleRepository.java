package com.example.labtrack.repository;

import com.example.labtrack.domain.Role;
import com.example.labtrack.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * RoleRepository provides database access operations for the Role entity.
 *
 * Extends JpaRepository to inherit standard CRUD operations (save, findById,
 * findAll, delete, etc.) without requiring any boilerplate implementation.
 *
 * The primary use case for this repository is looking up a Role by its
 * RoleType name during user registration and user updates. For example,
 * when a new student registers, UserServiceImpl calls findByName(RoleType.STUDENT)
 * to retrieve the correct Role entity and assign it to the new user.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a Role by its RoleType enum value.
     *
     * Spring Data JPA generates the query automatically based on the method name:
     * SELECT * FROM roles WHERE name = :name
     *
     * Returns an Optional to force the caller to handle the case where the
     * requested role does not exist in the database.
     *
     * @param name the RoleType to search for (e.g., RoleType.STUDENT)
     * @return an Optional containing the Role if found, or empty if not
     */
    Optional<Role> findByName(RoleType name);
}
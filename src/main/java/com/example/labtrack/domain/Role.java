package com.example.labtrack.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Role represents a named role in the system (e.g., STUDENT, LAB_ASSISTANT, ADMINISTRATOR).
 *
 * Each role is linked to a set of Permission objects that define what actions
 * a user with this role is allowed to perform. The relationship between Role
 * and Permission is many-to-many, stored in the "role_permissions" join table.
 *
 * Roles are loaded eagerly when a user is authenticated so Spring Security
 * can evaluate permission checks without triggering lazy-load exceptions.
 *
 * This entity maps to the "roles" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    /**
     * The type/name of this role, stored as a string in the database.
     * Uses RoleType enum values (STUDENT, LAB_ASSISTANT, ADMINISTRATOR).
     * Must be unique — there can only be one row per role type.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 40)
    private RoleType name;

    /**
     * The set of permissions granted to this role.
     *
     * Loaded eagerly (FetchType.EAGER) so that all permissions are available
     * immediately when Spring Security loads the user's authorities at login.
     *
     * The join table "role_permissions" maps role_id → permission_id.
     * Initialized as an empty HashSet to avoid NullPointerExceptions
     * when a new Role is created before any permissions are assigned.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();
}
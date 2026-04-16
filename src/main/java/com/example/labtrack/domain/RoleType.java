package com.example.labtrack.domain;

/**
 * RoleType defines the three user roles supported by the LabTrack system.
 *
 * These values are stored as strings in the "roles" table (via @Enumerated(EnumType.STRING))
 * and are used throughout the application for access control and permission checks.
 *
 * Role hierarchy (from least to most privileged):
 *  - STUDENT        : can reserve equipment, view own fines, submit damage reports
 *  - LAB_ASSISTANT  : can manage equipment and reservations, view fines
 *  - ADMINISTRATOR  : full access including user management and reports
 *
 * The actual permissions associated with each role are defined in the
 * "role_permissions" join table and loaded via the Role entity.
 */
public enum RoleType {

    /** Default role assigned to newly registered users. */
    STUDENT,

    /** Staff role with equipment and reservation management privileges. */
    LAB_ASSISTANT,

    /** Highest-privilege role with full system access. */
    ADMINISTRATOR
}
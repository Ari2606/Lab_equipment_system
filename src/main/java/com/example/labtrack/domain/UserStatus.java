package com.example.labtrack.domain;

/**
 * UserStatus represents the current state of a user's account in the system.
 *
 * This enum is stored as a string in the "users" table
 * (via @Enumerated(EnumType.STRING) on the User entity).
 *
 * Status is checked during authentication:
 *  - ACTIVE   : user can log in and use the system normally
 *  - INACTIVE : user is blocked from logging in (account suspended by admin)
 *
 * Using a status field instead of deleting the user preserves all historical
 * data (reservations, damage reports, fines) linked to that account.
 */
public enum UserStatus {

    /** Account is enabled. The user can log in and access the system. */
    ACTIVE,

    /** Account is suspended. The user cannot log in until an admin re-activates it. */
    INACTIVE
}
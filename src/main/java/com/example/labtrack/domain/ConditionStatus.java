package com.example.labtrack.domain;

/**
 * Enum representing the current condition of lab equipment.
 * Used to track availability and usability status.
 */
public enum ConditionStatus {

    /** Equipment is in good working condition */
    GOOD,

    /** Equipment is damaged and may require repair or replacement */
    DAMAGED,

    /** Equipment is currently under maintenance and not available for use */
    UNDER_MAINTENANCE
}

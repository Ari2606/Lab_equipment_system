package com.example.labtrack.domain;

/**
 * Enum representing the possible statuses of a fine.
 * Used to track payment and resolution state.
 */
public enum FineStatus {

    /** Fine has been issued but not yet paid */
    UNPAID,

    /** Fine has been successfully paid */
    PAID,

    /** Fine has been waived and is no longer applicable */
    WAIVED
}

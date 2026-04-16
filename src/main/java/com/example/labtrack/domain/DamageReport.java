package com.example.labtrack.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a damage report for lab equipment.
 * Stores details about the equipment, reporting user,
 * verification status, and related reservation if applicable.
 */
@Getter
@Setter
@Entity
@Table(name = "damage_reports")
public class DamageReport extends BaseEntity {

    /**
     * The equipment that has been reported as damaged.
     * This is a mandatory relationship.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    /**
     * The user who reported the damage.
     * This field is required for tracking accountability.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    /**
     * The user who verified the damage report.
     * This field is optional and set during verification.
     */
    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    /**
     * The reservation associated with the damage, if any.
     * Helps link damage to a specific usage instance.
     */
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    /**
     * Description of the damage provided by the reporter.
     * Limited to 500 characters.
     */
    @Column(nullable = false, length = 500)
    private String description;

    /**
     * Indicates whether the damage was caused due to misuse.
     * Default is false until verified.
     */
    @Column(nullable = false)
    private boolean misuseConfirmed = false;

    /**
     * Indicates whether the damage report has been verified.
     * Default is false until verification is completed.
     */
    @Column(nullable = false)
    private boolean verified = false;
}

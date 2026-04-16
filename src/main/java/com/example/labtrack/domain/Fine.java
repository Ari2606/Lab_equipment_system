package com.example.labtrack.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entity representing a fine imposed on a student.
 * A fine can be associated with either a reservation,
 * a damage report, or both depending on the violation.
 */
@Getter
@Setter
@Entity
@Table(name = "fines")
public class Fine extends BaseEntity {

    /**
     * The student who is being fined.
     * This is a mandatory relationship.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * The reservation associated with the fine, if applicable.
     * Used when fines are related to reservation misuse.
     */
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    /**
     * The damage report linked to this fine, if applicable.
     * Used when fines are issued due to equipment damage.
     */
    @ManyToOne
    @JoinColumn(name = "damage_report_id")
    private DamageReport damageReport;

    /**
     * The monetary amount of the fine.
     * Precision allows up to 10 digits with 2 decimal places.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * The reason for issuing the fine.
     * Provides context such as damage, late return, etc.
     */
    @Column(nullable = false, length = 255)
    private String reason;

    /**
     * Current status of the fine (e.g., UNPAID, PAID).
     * Stored as a string representation of the enum.
     * Defaults to UNPAID.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FineStatus status = FineStatus.UNPAID;
}

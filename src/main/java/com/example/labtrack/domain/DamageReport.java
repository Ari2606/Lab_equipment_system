package com.example.labtrack.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "damage_reports")
public class DamageReport extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private boolean misuseConfirmed = false;

    @Column(nullable = false)
    private boolean verified = false;
}

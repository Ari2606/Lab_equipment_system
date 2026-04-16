// JPA entity representing equipment with category, lab, and status fields
package com.example.labtrack.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "equipment")
public class Equipment extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String equipmentCode;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private EquipmentCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EquipmentAvailabilityStatus availabilityStatus = EquipmentAvailabilityStatus.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConditionStatus conditionStatus = ConditionStatus.GOOD;

    @Column(length = 255)
    private String notes;
}

// JPA entity for equipment categories with name and description
package com.example.labtrack.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "equipment_categories")
public class EquipmentCategory extends BaseEntity {
    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 255)
    private String description;
}

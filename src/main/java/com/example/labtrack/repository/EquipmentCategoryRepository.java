// Repository for EquipmentCategory CRUD operations
package com.example.labtrack.repository;

import com.example.labtrack.domain.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {}

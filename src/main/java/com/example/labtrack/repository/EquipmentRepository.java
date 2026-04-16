package com.example.labtrack.repository;

import com.example.labtrack.domain.ConditionStatus;
import com.example.labtrack.domain.Equipment;
import com.example.labtrack.domain.EquipmentAvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByNameContainingIgnoreCase(String name);
    List<Equipment> findByAvailabilityStatusAndConditionStatus(EquipmentAvailabilityStatus availabilityStatus, ConditionStatus conditionStatus);
}

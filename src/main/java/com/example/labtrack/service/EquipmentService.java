// Service interface defining equipment CRUD operations
package com.example.labtrack.service;

import com.example.labtrack.domain.Equipment;
import com.example.labtrack.dto.EquipmentForm;

import java.util.List;

public interface EquipmentService {
    List<Equipment> findAll();
    Equipment findById(Long id);
    Equipment create(EquipmentForm form);
    Equipment update(Long id, EquipmentForm form);
    void delete(Long id);
}

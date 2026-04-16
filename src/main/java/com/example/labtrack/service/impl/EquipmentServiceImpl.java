package com.example.labtrack.service.impl;

import com.example.labtrack.domain.Equipment;
import com.example.labtrack.dto.EquipmentForm;
import com.example.labtrack.repository.EquipmentCategoryRepository;
import com.example.labtrack.repository.EquipmentRepository;
import com.example.labtrack.repository.LabRepository;
import com.example.labtrack.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository categoryRepository;
    private final LabRepository labRepository;

    @Override public List<Equipment> findAll() { return equipmentRepository.findAll(); }

    @Override public Equipment findById(Long id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Equipment not found"));
    }

    @Override
    public Equipment create(EquipmentForm form) {
        Equipment equipment = new Equipment();
        apply(form, equipment);
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment update(Long id, EquipmentForm form) {
        Equipment equipment = findById(id);
        apply(form, equipment);
        return equipmentRepository.save(equipment);
    }

    @Override
    public void delete(Long id) { equipmentRepository.deleteById(id); }

    private void apply(EquipmentForm form, Equipment equipment) {
        equipment.setEquipmentCode(form.getEquipmentCode());
        equipment.setName(form.getName());
        equipment.setCategory(categoryRepository.findById(form.getCategoryId()).orElseThrow());
        equipment.setLab(labRepository.findById(form.getLabId()).orElseThrow());
        equipment.setAvailabilityStatus(form.getAvailabilityStatus());
        equipment.setConditionStatus(form.getConditionStatus());
        equipment.setNotes(form.getNotes());
    }
}

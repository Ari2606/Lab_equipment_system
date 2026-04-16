package com.example.labtrack.controller;

import com.example.labtrack.domain.ConditionStatus;
import com.example.labtrack.domain.EquipmentAvailabilityStatus;
import com.example.labtrack.dto.EquipmentForm;
import com.example.labtrack.repository.EquipmentCategoryRepository;
import com.example.labtrack.repository.LabRepository;
import com.example.labtrack.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final EquipmentCategoryRepository categoryRepository;
    private final LabRepository labRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('EQUIPMENT_VIEW')")
    public String list(Model model) {
        model.addAttribute("equipmentList", equipmentService.findAll());
        return "equipment/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('EQUIPMENT_CREATE')")
    public String createForm(Model model) {
        populate(model, new EquipmentForm());
        return "equipment/form";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EQUIPMENT_CREATE')")
    public String create(@Valid @ModelAttribute EquipmentForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            populate(model, form);
            return "equipment/form";
        }
        equipmentService.create(form);
        return "redirect:/equipment";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('EQUIPMENT_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {
        var equipment = equipmentService.findById(id);
        EquipmentForm form = new EquipmentForm();
        form.setEquipmentCode(equipment.getEquipmentCode());
        form.setName(equipment.getName());
        form.setCategoryId(equipment.getCategory().getId());
        form.setLabId(equipment.getLab().getId());
        form.setAvailabilityStatus(equipment.getAvailabilityStatus());
        form.setConditionStatus(equipment.getConditionStatus());
        form.setNotes(equipment.getNotes());
        populate(model, form);
        model.addAttribute("equipmentId", id);
        return "equipment/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('EQUIPMENT_UPDATE')")
    public String update(@PathVariable Long id, @Valid @ModelAttribute EquipmentForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            populate(model, form);
            model.addAttribute("equipmentId", id);
            return "equipment/form";
        }
        equipmentService.update(id, form);
        return "redirect:/equipment";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('EQUIPMENT_DELETE')")
    public String delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return "redirect:/equipment";
    }

    private void populate(Model model, EquipmentForm form) {
        model.addAttribute("equipmentForm", form);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("labs", labRepository.findAll());
        model.addAttribute("availabilityStatuses", EquipmentAvailabilityStatus.values());
        model.addAttribute("conditionStatuses", ConditionStatus.values());
    }
}

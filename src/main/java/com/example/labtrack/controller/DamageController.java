package com.example.labtrack.controller;

import com.example.labtrack.config.SecurityUserDetails;
import com.example.labtrack.dto.DamageReportForm;
import com.example.labtrack.service.DamageService;
import com.example.labtrack.service.EquipmentService;
import com.example.labtrack.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/damage")
public class DamageController {
    private final DamageService damageService;
    private final EquipmentService equipmentService;
    private final ReservationService reservationService;

    @GetMapping
    @PreAuthorize("hasAuthority('DAMAGE_VIEW')")
    public String list(Model model) {
        model.addAttribute("damageReports", damageService.findAll());
        return "damage/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('DAMAGE_REPORT')")
    public String form(Model model) {
        model.addAttribute("damageReportForm", new DamageReportForm());
        model.addAttribute("equipmentList", equipmentService.findAll());
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "damage/form";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DAMAGE_REPORT')")
    public String create(@Valid @ModelAttribute DamageReportForm form,
                         BindingResult result,
                         @AuthenticationPrincipal SecurityUserDetails principal,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("equipmentList", equipmentService.findAll());
            model.addAttribute("reservations", reservationService.getAllReservations());
            return "damage/form";
        }
        damageService.reportDamage(form, principal.getUser());
        return "redirect:/damage";
    }

    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('DAMAGE_VERIFY')")
    public String verify(@PathVariable Long id,
                         @RequestParam(defaultValue = "false") boolean misuse,
                         @AuthenticationPrincipal SecurityUserDetails principal) {
        damageService.verify(id, misuse, principal.getUser());
        return "redirect:/damage";
    }
}

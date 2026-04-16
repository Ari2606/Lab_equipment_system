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

/**
 * Controller responsible for handling all damage-related operations
 * such as viewing reports, creating new reports, and verifying damage.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/damage")
public class DamageController {

    // Service layer dependencies for handling business logic
    private final DamageService damageService;
    private final EquipmentService equipmentService;
    private final ReservationService reservationService;

    /**
     * Displays a list of all damage reports.
     * Accessible only to users with DAMAGE_VIEW authority.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('DAMAGE_VIEW')")
    public String list(Model model) {
        // Fetch all damage reports and add to model for view rendering
        model.addAttribute("damageReports", damageService.findAll());
        return "damage/list";
    }

    /**
     * Displays the form to report new damage.
     * Populates required dropdowns like equipment and reservations.
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('DAMAGE_REPORT')")
    public String form(Model model) {
        // Add empty form object for user input
        model.addAttribute("damageReportForm", new DamageReportForm());

        // Populate equipment and reservation data for selection
        model.addAttribute("equipmentList", equipmentService.findAll());
        model.addAttribute("reservations", reservationService.getAllReservations());

        return "damage/form";
    }

    /**
     * Handles submission of a new damage report.
     * Validates input and persists the report if valid.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('DAMAGE_REPORT')")
    public String create(@Valid @ModelAttribute DamageReportForm form,
                         BindingResult result,
                         @AuthenticationPrincipal SecurityUserDetails principal,
                         Model model) {

        // If validation fails, reload form with required data
        if (result.hasErrors()) {
            model.addAttribute("equipmentList", equipmentService.findAll());
            model.addAttribute("reservations", reservationService.getAllReservations());
            return "damage/form";
        }

        // Save damage report with the authenticated user
        damageService.reportDamage(form, principal.getUser());

        // Redirect to damage report list after successful submission
        return "redirect:/damage";
    }

    /**
     * Verifies a damage report.
     * Optionally marks it as misuse based on input parameter.
     */
    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('DAMAGE_VERIFY')")
    public String verify(@PathVariable Long id,
                         @RequestParam(defaultValue = "false") boolean misuse,
                         @AuthenticationPrincipal SecurityUserDetails principal) {

        // Perform verification logic with misuse flag and current user
        damageService.verify(id, misuse, principal.getUser());

        // Redirect back to damage report list
        return "redirect:/damage";
    }
}

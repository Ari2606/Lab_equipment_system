package com.example.labtrack.controller;

import com.example.labtrack.config.SecurityUserDetails;
import com.example.labtrack.dto.ReservationForm;
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
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final EquipmentService equipmentService;

    @GetMapping
    @PreAuthorize("hasAuthority('RESERVATION_VIEW_ALL') or hasAuthority('RESERVATION_VIEW_OWN')")
    public String list(@AuthenticationPrincipal SecurityUserDetails principal, Model model) {
        boolean student = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("RESERVATION_VIEW_OWN"));
        model.addAttribute("reservations", student ? reservationService.getReservationsForStudent(principal.getUser()) : reservationService.getAllReservations());
        return "reservations/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('RESERVATION_CREATE')")
    public String createForm(Model model) {
        model.addAttribute("reservationForm", new ReservationForm());
        model.addAttribute("equipmentList", equipmentService.findAll());
        return "reservations/form";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RESERVATION_CREATE')")
    public String create(@Valid @ModelAttribute ReservationForm form,
                         BindingResult result,
                         @AuthenticationPrincipal SecurityUserDetails principal,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("equipmentList", equipmentService.findAll());
            return "reservations/form";
        }
        try {
            reservationService.createReservation(form, principal.getUser());
            return "redirect:/reservations";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("equipmentList", equipmentService.findAll());
            return "reservations/form";
        }
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('RESERVATION_APPROVE')")
    public String approve(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDetails principal) {
        reservationService.approve(id, principal.getUser());
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('RESERVATION_APPROVE')")
    public String reject(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDetails principal) {
        reservationService.reject(id, principal.getUser());
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/issue")
    @PreAuthorize("hasAuthority('ISSUE_RETURN_PROCESS')")
    public String issue(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDetails principal) {
        reservationService.issue(id, principal.getUser());
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAuthority('ISSUE_RETURN_PROCESS')")
    public String markReturned(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDetails principal) {
        reservationService.markReturned(id, principal.getUser());
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('RESERVATION_CANCEL_OWN')")
    public String cancel(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDetails principal) {
        reservationService.cancel(id, principal.getUser());
        return "redirect:/reservations";
    }
}

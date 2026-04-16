package com.example.labtrack.controller;

import com.example.labtrack.config.SecurityUserDetails;
import com.example.labtrack.service.DamageService;
import com.example.labtrack.service.EquipmentService;
import com.example.labtrack.service.FineService;
import com.example.labtrack.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final EquipmentService equipmentService;
    private final ReservationService reservationService;
    private final DamageService damageService;
    private final FineService fineService;

    @GetMapping("/")
    public String root() { return "redirect:/dashboard"; }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal SecurityUserDetails principal, Model model) {
        model.addAttribute("currentUser", principal.getUser());
        model.addAttribute("equipmentCount", equipmentService.findAll().size());
        model.addAttribute("reservationCount", reservationService.getAllReservations().size());
        model.addAttribute("damageCount", damageService.findAll().size());
        model.addAttribute("fineCount", fineService.allFines().size());
        return "dashboard";
    }

    @GetMapping("/access-denied")
    public String accessDenied() { return "access-denied"; }
}

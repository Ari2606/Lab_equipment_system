package com.example.labtrack.controller;

import com.example.labtrack.config.SecurityUserDetails;
import com.example.labtrack.domain.RoleType;
import com.example.labtrack.domain.UserStatus;
import com.example.labtrack.dto.UserUpdateForm;
import com.example.labtrack.repository.AuditLogRepository;
import com.example.labtrack.service.FineService;
import com.example.labtrack.service.UserService;
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
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN_USER_MANAGE') or hasAuthority('REPORT_VIEW') or hasAuthority('FINE_VIEW_OWN') or hasAuthority('FINE_MARK_PAID')")
public class AdminController {

    private final UserService userService;
    private final FineService fineService;
    private final AuditLogRepository auditLogRepository;

    // ================= USERS =================

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN_USER_MANAGE')")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", RoleType.values());
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("userUpdateForm", new UserUpdateForm());
        return "admin/users";
    }

    @PostMapping("/users/update")
    @PreAuthorize("hasAuthority('ADMIN_USER_MANAGE')")
    public String update(@Valid @ModelAttribute UserUpdateForm form,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return users(model);
        }
        userService.updateUser(form);
        return "redirect:/admin/users";
    }

    // ================= FINES =================

    @GetMapping("/fines")
    @PreAuthorize("hasAuthority('FINE_MARK_PAID') or hasAuthority('FINE_VIEW_OWN')")
    public String fines(@AuthenticationPrincipal SecurityUserDetails principal,
                        Model model) {

        // Admin / Lab Assistant → see all fines
        if (principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("FINE_MARK_PAID"))) {

            model.addAttribute("fines", fineService.allFines());

        } 
        // Student → see only their fines
        else {
            model.addAttribute("fines",
                    fineService.studentFines(principal.getUser()));
        }

        return "admin/fines";
    }

    @PostMapping("/fines/{id}/paid")
    @PreAuthorize("hasAuthority('FINE_MARK_PAID')")
    public String markPaid(@PathVariable Long id) {
        fineService.markPaid(id);
        return "redirect:/admin/fines";
    }

    // ================= REPORTS =================

    @GetMapping("/reports")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public String reports(Model model) {
        model.addAttribute("logs", auditLogRepository.findAll());
        model.addAttribute("fines", fineService.allFines());
        model.addAttribute("users", userService.findAll());
        return "reports/index";
    }
}
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

/**
 * AdminController handles all admin-facing HTTP requests under the /admin route.
 *
 * This controller manages three main functional areas:
 *   1. User management  - viewing and updating user roles/statuses
 *   2. Fines management - viewing and marking fines as paid
 *   3. Reports          - viewing audit logs and fine summaries
 *
 * Access is restricted using Spring Security's @PreAuthorize annotation.
 * Each endpoint checks for the relevant authority (e.g., ADMIN_USER_MANAGE,
 * FINE_MARK_PAID, REPORT_VIEW) before allowing access.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN_USER_MANAGE') or hasAuthority('REPORT_VIEW') or hasAuthority('FINE_VIEW_OWN') or hasAuthority('FINE_MARK_PAID')")
public class AdminController {

    // Service for user-related operations (list, update, register)
    private final UserService userService;

    // Service for fine-related operations (list all, list by student, mark paid)
    private final FineService fineService;

    // Repository for fetching audit log entries displayed in the reports page
    private final AuditLogRepository auditLogRepository;

    // ================= USERS =================

    /**
     * Displays the user management page with a list of all registered users.
     *
     * Populates the model with:
     *  - users: all users in the system
     *  - roles: all available RoleType enum values (for the update form dropdown)
     *  - statuses: all available UserStatus enum values (for the update form dropdown)
     *  - userUpdateForm: empty form object bound to the Thymeleaf template
     *
     * Only accessible to users with ADMIN_USER_MANAGE authority.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN_USER_MANAGE')")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", RoleType.values());
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("userUpdateForm", new UserUpdateForm());
        return "admin/users";
    }

    /**
     * Handles the form submission for updating a user's role or status.
     *
     * Validates the submitted UserUpdateForm. If validation fails,
     * the user management page is re-rendered with error messages.
     * On success, delegates to UserService to apply the changes,
     * then redirects back to the users list.
     *
     * Only accessible to users with ADMIN_USER_MANAGE authority.
     */
    @PostMapping("/users/update")
    @PreAuthorize("hasAuthority('ADMIN_USER_MANAGE')")
    public String update(@Valid @ModelAttribute UserUpdateForm form,
                         BindingResult result,
                         Model model) {
        // Return to the form with validation errors if any field is invalid
        if (result.hasErrors()) {
            return users(model);
        }
        userService.updateUser(form);
        return "redirect:/admin/users";
    }

    // ================= FINES =================

    /**
     * Displays the fines page with role-based filtering.
     *
     * The currently logged-in user's authorities determine what they see:
     *  - Admin / Lab Assistant (FINE_MARK_PAID authority): sees all fines in the system
     *  - Student (FINE_VIEW_OWN authority only): sees only their own fines
     *
     * This conditional logic ensures students cannot view other users' fine data.
     */
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

    /**
     * Marks a specific fine as paid, identified by its ID in the URL path.
     *
     * Delegates to FineService to update the fine's status, then redirects
     * back to the fines list. Only users with FINE_MARK_PAID authority
     * (Admin or Lab Assistant) can perform this action.
     */
    @PostMapping("/fines/{id}/paid")
    @PreAuthorize("hasAuthority('FINE_MARK_PAID')")
    public String markPaid(@PathVariable Long id) {
        fineService.markPaid(id);
        return "redirect:/admin/fines";
    }

    // ================= REPORTS =================

    /**
     * Displays the system reports page, combining audit logs, fines, and user data.
     *
     * Populates the model with:
     *  - logs: all audit log entries (tracks user actions across the system)
     *  - fines: all fines (for reporting and summary purposes)
     *  - users: all users (for cross-referencing in the report view)
     *
     * Only accessible to users with REPORT_VIEW authority.
     */
    @GetMapping("/reports")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public String reports(Model model) {
        model.addAttribute("logs", auditLogRepository.findAll());
        model.addAttribute("fines", fineService.allFines());
        model.addAttribute("users", userService.findAll());
        return "reports/index";
    }
}
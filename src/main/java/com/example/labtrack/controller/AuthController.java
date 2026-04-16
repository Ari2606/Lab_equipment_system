package com.example.labtrack.controller;

import com.example.labtrack.dto.RegisterRequest;
import com.example.labtrack.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * AuthController handles authentication-related routes: login and registration.
 *
 * Login is delegated entirely to Spring Security (configured in SecurityConfig).
 * This controller only provides the GET mapping to render the login view.
 *
 * Registration is handled here: it renders the registration form on GET,
 * and processes the submitted form on POST by calling UserService.registerStudent().
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    // UserService handles business logic for registration (validation, hashing, role assignment)
    private final UserService userService;

    /**
     * Renders the login page.
     *
     * Spring Security intercepts POST /login automatically, so no POST
     * mapping is needed here. This GET simply returns the login template.
     */
    @GetMapping("/login")
    public String login() { return "auth/login"; }

    /**
     * Renders the student registration form.
     *
     * Adds an empty RegisterRequest object to the model so Thymeleaf
     * can bind the form fields to it.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    /**
     * Processes the registration form submission.
     *
     * Validates the submitted RegisterRequest using Bean Validation (@Valid).
     * If validation fails, the registration form is re-displayed with errors.
     *
     * On success, delegates to UserService.registerStudent() which:
     *   - Checks for duplicate username / email
     *   - Encodes the password with BCrypt
     *   - Assigns the default STUDENT role
     *   - Persists the new user
     *
     * After successful registration, redirects to /login with a "registered"
     * query parameter that the login page uses to display a success message.
     *
     * If a runtime exception occurs (e.g., duplicate username/email),
     * the error message is added to the model and the form is re-displayed.
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult result,
                           Model model) {
        // Re-render form if @Valid constraints are violated
        if (result.hasErrors()) return "auth/register";
        try {
            userService.registerStudent(registerRequest);
            return "redirect:/login?registered";
        } catch (Exception ex) {
            // Catches service-level exceptions such as duplicate username/email
            model.addAttribute("error", ex.getMessage());
            return "auth/register";
        }
    }
}
package com.example.labtrack.dto;

import com.example.labtrack.domain.RoleType;
import com.example.labtrack.domain.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * UserUpdateForm is a Data Transfer Object (DTO) that carries the data
 * submitted from the admin user-management form when updating a user's
 * role or account status.
 *
 * It is bound to the Thymeleaf form on the admin/users page and passed
 * to AdminController.update(), which delegates to UserService.updateUser().
 *
 * All three fields are required (@NotNull). If any field is missing,
 * Spring's Bean Validation returns an error and the form is re-rendered.
 *
 * This DTO intentionally contains only the fields an admin is allowed
 * to change (userId, roleType, status), keeping other user data (e.g.,
 * password, email) protected from unintended modification via this form.
 */
@Getter
@Setter
public class UserUpdateForm {

    /**
     * The ID of the user to be updated.
     * Used by UserService to look up the correct User entity in the database.
     */
    @NotNull
    private Long userId;

    /**
     * The new role to assign to the user (STUDENT, LAB_ASSISTANT, or ADMINISTRATOR).
     * UserService will look up the matching Role entity and update the user's role.
     */
    @NotNull
    private RoleType roleType;

    /**
     * The new account status to set (ACTIVE or INACTIVE).
     * Setting to INACTIVE suspends the user's access without deleting their data.
     */
    @NotNull
    private UserStatus status;
}
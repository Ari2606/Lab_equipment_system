package com.example.labtrack.dto;

import com.example.labtrack.domain.RoleType;
import com.example.labtrack.domain.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateForm {
    @NotNull
    private Long userId;
    @NotNull
    private RoleType roleType;
    @NotNull
    private UserStatus status;
}

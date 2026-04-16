package com.example.labtrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DamageReportForm {
    @NotNull
    private Long equipmentId;
    private Long reservationId;
    @NotBlank
    private String description;
}

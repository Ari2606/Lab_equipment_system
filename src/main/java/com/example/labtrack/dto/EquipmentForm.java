// DTO for equipment form input with validation constraints
package com.example.labtrack.dto;

import com.example.labtrack.domain.ConditionStatus;
import com.example.labtrack.domain.EquipmentAvailabilityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentForm {
    @NotBlank
    private String equipmentCode;
    @NotBlank
    private String name;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long labId;
    @NotNull
    private EquipmentAvailabilityStatus availabilityStatus;
    @NotNull
    private ConditionStatus conditionStatus;
    private String notes;
}

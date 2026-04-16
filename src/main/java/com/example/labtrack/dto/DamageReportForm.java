package com.example.labtrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) used for capturing
 * damage report form input from the user.
 */
@Getter
@Setter
public class DamageReportForm {

    /**
     * ID of the equipment being reported as damaged.
     * This field is required.
     */
    @NotNull
    private Long equipmentId;

    /**
     * ID of the associated reservation, if any.
     * This field is optional.
     */
    private Long reservationId;

    /**
     * Description of the damage provided by the user.
     * This field must not be blank.
     */
    @NotBlank
    private String description;
}

package com.issoft.ticketstoreapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long id;

    @NotBlank(message = "title can not be empty")
    private String title;

    private Short length;

    private Boolean is3D;

    private Boolean isIMAX;

    private Short daysInRotation;
}

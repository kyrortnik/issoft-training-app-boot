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
public class HallDTO {

    private Long id;

    private CinemaDTO cinema;

    @NotBlank(message = "hall name can not be empty")
    private String name;

    private Boolean has3DSupport;

    private Boolean hasIMAXSupport;
}
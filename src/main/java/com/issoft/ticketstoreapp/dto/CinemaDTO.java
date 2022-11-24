package com.issoft.ticketstoreapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CinemaDTO {

    private Long id;

    @NotBlank(message = "cinema name can not be empty")
    private String name;

    private String startWorkTime;

    private String closeWorkTime;

    private List<MovieDTO> moviesInRotation = new ArrayList<>();
}
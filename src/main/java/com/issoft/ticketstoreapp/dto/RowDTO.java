package com.issoft.ticketstoreapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RowDTO {

    private Long id;

    @NotNull(message = "row number can not be empty")
    private Short number;

    private HallDTO hall;
}
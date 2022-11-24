package com.issoft.ticketstoreapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {

    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Seat number cannot be null")
    private Short number;

    private RowDTO row;
}
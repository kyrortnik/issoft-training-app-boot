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
public class TicketDTO {

    private Long id;

    private String code;

    private UserDTO buyer;

    @NotNull(message = "need to specify hall")
    private HallDTO hall;

    @NotNull
    private SeatDTO seat;

    @NotNull(message = "need to specify movie")
    private MovieDTO movie;

    @NotNull(message = "need to specify session date and time")
    private String sessionDateTime;

    private String purchaseDateTime;
}
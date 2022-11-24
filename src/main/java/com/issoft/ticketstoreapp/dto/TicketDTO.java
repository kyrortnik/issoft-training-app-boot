package com.issoft.ticketstoreapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;

    @NotBlank(message = "code can not de empty")
    private String code;

    private UserDTO buyer;

    private HallDTO hall;

    private SeatDTO seat;

    private MovieDTO movie;

    @NotNull(message = "need to specify session date and time")
    private String sessionDateTime;

    @NotNull(message = "need to specify purchase date and time")
    private String purchaseDateTime;
}
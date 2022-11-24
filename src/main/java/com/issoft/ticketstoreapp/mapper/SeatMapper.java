package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.SeatDTO;
import com.issoft.ticketstoreapp.model.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    SeatDTO toDto(Seat seat);

    Seat toSeat(SeatDTO seatDTO);
}
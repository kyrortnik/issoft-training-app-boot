package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.HallDTO;
import com.issoft.ticketstoreapp.model.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HallMapper {

    HallDTO toDto(Hall hall);

    Hall toHall(HallDTO hallDTO);
}
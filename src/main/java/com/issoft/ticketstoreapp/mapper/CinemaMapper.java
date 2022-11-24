package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.CinemaDTO;
import com.issoft.ticketstoreapp.model.Cinema;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaMapper {

    CinemaDTO toDto(Cinema cinema);

    Cinema toCinema(CinemaDTO cinemaDTO);
}
package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.MovieDTO;
import com.issoft.ticketstoreapp.model.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieDTO toDto(Movie movie);

    Movie toMovie(MovieDTO movieDTO);
}
package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.RowDTO;
import com.issoft.ticketstoreapp.model.Row;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RowMapper {

    RowDTO toDto(Row row);

    Row toRow(RowDTO rowDTO);
}
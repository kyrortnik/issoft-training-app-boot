package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.WeatherReportDTO;
import com.issoft.ticketstoreapp.model.weather.WeatherReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherReportMapper {

    WeatherReportDTO toDto(WeatherReport weatherReport);

    WeatherReport toWeatherReport(WeatherReportDTO weatherReportDTO);
}
package com.issoft.ticketstoreapp.service;


import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.WeatherReportDTO;
import com.issoft.ticketstoreapp.mapper.WeatherReportMapper;
import com.issoft.ticketstoreapp.model.weather.WeatherReport;
import com.issoft.ticketstoreapp.repository.WeatherReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WeatherReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherReportService.class);

    private final WeatherReportRepository weatherReportRepository;
    private final WeatherReportMapper mapper;

    @Audit
    public WeatherReportDTO save(WeatherReportDTO weatherReportDTO) {
        LOGGER.debug("start in WeatherReportService.save()");
        WeatherReport weatherReport = this.mapper.toWeatherReport(weatherReportDTO);

        WeatherReportDTO savedWeatherReport = mapper.toDto(this.weatherReportRepository.save(weatherReport));

        LOGGER.debug("end in WeatherReportService.save()");
        return savedWeatherReport;
    }

    @Audit
    public List<WeatherReportDTO> findWeatherReports() {
        LOGGER.debug("start in WeatherReportService.getWeatherReports()");
        List<WeatherReportDTO> weatherReports = this.weatherReportRepository.findAll()
                .stream()
                .map(this.mapper::toDto)
                .collect(Collectors.toList());

        LOGGER.debug("end in WeatherReportService.getWeatherReports()");
        return weatherReports;
    }

    @Audit
    public void deleteWeatherReport(Long weatherReportId) {
        LOGGER.debug("start in WeatherReportService.deleteWeatherReport()");

        this.weatherReportRepository.deleteById(weatherReportId);

        LOGGER.debug("end in WeatherReportService.deleteWeatherReport()");
    }
}
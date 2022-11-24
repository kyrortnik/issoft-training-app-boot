package com.issoft.ticketstoreapp.task;

import com.issoft.ticketstoreapp.client.WeatherApiClient;
import com.issoft.ticketstoreapp.dto.WeatherReportDTO;
import com.issoft.ticketstoreapp.service.WeatherReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.jobs.enabled", havingValue = "true")
public class WeatherTask {
    private final WeatherReportService weatherReportService;
    private final WeatherApiClient weatherApiClient;

    @Scheduled(fixedDelayString = "${scheduled.fixedDelay.in.milliseconds}")
    public WeatherReportDTO saveTbilisiWeather() {

        return this.weatherReportService.save(getTbilisiWeather());
    }

    public WeatherReportDTO getTbilisiWeather() {

        return this.weatherApiClient.getTbilisiWeather();
    }
}
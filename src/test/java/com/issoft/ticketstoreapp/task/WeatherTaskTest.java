package com.issoft.ticketstoreapp.task;

import com.issoft.ticketstoreapp.client.WeatherApiClient;
import com.issoft.ticketstoreapp.dto.WeatherReportDTO;
import com.issoft.ticketstoreapp.listener.MockResetListener;
import com.issoft.ticketstoreapp.service.WeatherReportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;


@TestExecutionListeners(
        value = {MockResetListener.class},
        mergeMode = MERGE_WITH_DEFAULTS)
@SpringBootTest
class WeatherTaskTest {

    public static final String GEORGIA = "GE";

    @Autowired
    private WeatherReportService weatherReportService;
    @Autowired
    private WeatherApiClient weatherApiClient;
    private WeatherTask weatherTask;
    private WeatherReportDTO testWeatherReportDTO;

    @BeforeEach
    void setUp() {
        this.weatherTask = new WeatherTask(this.weatherReportService, this.weatherApiClient);

        this.testWeatherReportDTO = WeatherReportDTO.builder()
                .dt(Instant.now())
                .country(GEORGIA)
                .build();
    }

    @AfterEach
    void tearDown() {
        this.weatherTask = null;
        this.testWeatherReportDTO = null;
    }

    @Test
    void test_saveTbilisiWeather_realWeatherApiInvoked() {
        WeatherReportDTO savedWeatherReport = this.weatherTask.saveTbilisiWeather();
        List<WeatherReportDTO> foundWeatherReports = this.weatherReportService.findWeatherReports();

        Assertions.assertEquals(savedWeatherReport, foundWeatherReports.get(0));

        this.weatherReportService.deleteWeatherReport(savedWeatherReport.getId());
    }

    @Test
    void test_saveTbilisiWeather_mockWeatherApiInvoked() {
        when(this.weatherApiClient.getTbilisiWeather()).thenReturn(this.testWeatherReportDTO);
        WeatherReportDTO actualWeatherReport = this.weatherTask.saveTbilisiWeather();

        Assertions.assertEquals(testWeatherReportDTO.getDt(), actualWeatherReport.getDt());

        this.weatherReportService.deleteWeatherReport(actualWeatherReport.getId());
    }
}
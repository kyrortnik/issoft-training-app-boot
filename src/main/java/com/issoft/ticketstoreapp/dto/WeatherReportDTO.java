package com.issoft.ticketstoreapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherReportDTO {
    public static final String TEMP = "temp";
    public static final String FEELS_LIKE = "feels_like";
    public static final String TEMP_MIN = "temp_min";
    public static final String TEMP_MAX = "temp_max";
    public static final String COUNTRY = "country";
    public static final String SUNRISE = "sunrise";
    public static final String SUNSET = "sunset";

    private Long id;

    private Instant dt;
    private Double temperature;
    private Double feelsLike;
    private Double temperatureMin;
    private Double temperatureMax;
    private Instant sunrise;
    private Instant sunset;
    private String country;

    @JsonProperty("main")
    private void setMain(Map<String, Object> main) {

        this.temperature = (Double) main.get(TEMP);
        this.feelsLike = (Double) main.get(FEELS_LIKE);
        this.temperatureMin = (Double) main.get(TEMP_MIN);
        this.temperatureMax = (Double) main.get(TEMP_MAX);
    }

    @JsonProperty("sys")
    private void setSystem(Map<String, Object> system) {

        this.country = (String) system.get(COUNTRY);
        this.sunrise = Instant.ofEpochSecond((Integer) system.get(SUNRISE));
        this.sunset = Instant.ofEpochSecond((Integer) system.get(SUNSET));
    }
}
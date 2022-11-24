package com.issoft.ticketstoreapp.client;

import com.issoft.ticketstoreapp.config.WeatherProperties;
import com.issoft.ticketstoreapp.dto.WeatherReportDTO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class WeatherApiClient {

    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String APPID = "appid";

    private final WebClient webClient;
    private final WeatherProperties weatherProperties;

    @Autowired
    public WeatherApiClient(WeatherProperties weatherProperties) {
        this.weatherProperties = weatherProperties;
        this.webClient = WebClient.builder()
                .baseUrl(weatherProperties.getBaseApiUri())
                .build();
    }

    public WeatherReportDTO getTbilisiWeather() {
        return webClient
                .get()
                .uri(this.weatherProperties.getApiUri(), getTbilisiWeatherParams())
                .retrieve()
                .bodyToMono(WeatherReportDTO.class)
                .block();
    }

    private Map<String, String> getTbilisiWeatherParams() {
        Map<String, String> weatherParams = new HashMap<>();
        weatherParams.put(LATITUDE, String.valueOf(this.weatherProperties.getTbilisiLat()));
        weatherParams.put(LONGITUDE, String.valueOf(this.weatherProperties.getTbilisiLon()));
        weatherParams.put(APPID, this.weatherProperties.getAppId());
        return weatherParams;
    }
}
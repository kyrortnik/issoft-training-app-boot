package com.issoft.ticketstoreapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weather")
@Setter
@Getter
public class WeatherProperties {

    private String baseApiUri;

    private String apiUri;

    private String appId;

    private Double tbilisiLat;

    private Double tbilisiLon;
}
package com.issoft.ticketstoreapp.model.weather;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "weather_reports")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private Instant dt;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "feels_like")
    private Double feelsLike;

    @Column(name = "temperature_min")
    private Double temperatureMin;

    @Column(name = "temperature_max")
    private Double temperatureMax;

    private Instant sunrise;

    private Instant sunset;

    private String country;
}
package com.issoft.ticketstoreapp.repository;

import com.issoft.ticketstoreapp.model.weather.WeatherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long> {
}
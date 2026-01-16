package com.flightontime.api.integration.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.flightontime.api.integration.weather.dto.OpenMeteoResponse;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
public class WeatherClient {

    private final RestTemplate restTemplate;
    private final String weatherApiUrl;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Open-Meteo API date limits
    private static final LocalDate MIN_DATE = LocalDate.of(2025, 10, 15);
    private static final LocalDate MAX_DATE = LocalDate.of(2026, 1, 31);

    public WeatherClient(
            RestTemplate restTemplate,
            @Value("${weather.api.url}") String weatherApiUrl) {
        this.restTemplate = restTemplate;
        this.weatherApiUrl = weatherApiUrl;
    }

    public OpenMeteoResponse getWeatherForecast(double latitude, double longitude, LocalDateTime dateTime) {
        LocalDate requestDate = dateTime.toLocalDate();
        
        // Validate date range - return null if out of range
        if (requestDate.isBefore(MIN_DATE) || requestDate.isAfter(MAX_DATE)) {
            log.warn("Requested date {} is outside API allowed range ({} to {}). Returning null.", 
                    requestDate, MIN_DATE, MAX_DATE);
            return null;
        }
        
        String date = dateTime.format(DATE_FORMATTER);
        int hour = dateTime.getHour();
        
        String url = String.format(Locale.US,
                "%s?latitude=%.6f&longitude=%.6f&hourly=temperature_2m,precipitation,wind_speed_10m&start_date=%s&end_date=%s&timezone=auto",
                weatherApiUrl, latitude, longitude, date, date);

        log.info("Calling Weather API: {}", url);

        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

        if (response != null && response.getHourly() != null) {
            response.setCurrent(extractHourlyData(response.getHourly(), hour));
        }

        log.info("Weather forecast retrieved for coordinates: {}, {} at {}", latitude, longitude, dateTime);
        return response;
    }

    private OpenMeteoResponse.CurrentWeather extractHourlyData(OpenMeteoResponse.HourlyWeather hourly, int requestedHour) {
        int index = Math.min(requestedHour, hourly.getTime().size() - 1);
        
        OpenMeteoResponse.CurrentWeather current = new OpenMeteoResponse.CurrentWeather();
        current.setTime(hourly.getTime().get(index));
        current.setTemperature2m(hourly.getTemperature2m().get(index));
        current.setPrecipitation(hourly.getPrecipitation().get(index));
        current.setWindSpeed10m(hourly.getWindSpeed10m().get(index));
        
        return current;
    }
}
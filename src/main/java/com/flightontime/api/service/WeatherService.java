package com.flightontime.api.service;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.flightontime.api.dto.WeatherDTO;
import com.flightontime.api.entity.Airport;
import com.flightontime.api.integration.weather.WeatherClient;
import com.flightontime.api.integration.weather.dto.OpenMeteoResponse;
import com.flightontime.api.repository.AirportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;
    private final AirportRepository airportRepository;

    public WeatherDTO getWeatherForAirport(String airportCode, LocalDateTime dateTime) {
        log.info("Fetching weather for airport: {} at {}", airportCode, dateTime);

        Airport airport = airportRepository.findByAirportCode(airportCode);
        
        log.info("Airport found: {} at coordinates ({}, {})",
                airport.getAirportName(),
                airport.getAirportLatitude(),
                airport.getAirportLongitude());

        OpenMeteoResponse weatherResponse = weatherClient.getWeatherForecast(
                airport.getAirportLatitude(),
                airport.getAirportLongitude(),
                dateTime);

        // Return null if weather data is unavailable (date out of range)
        if (weatherResponse == null) {
            log.info("Weather data unavailable for date: {}", dateTime);
            return null;
        }

        return formatWeatherData(weatherResponse, dateTime);
    }

    private WeatherDTO formatWeatherData(OpenMeteoResponse response, LocalDateTime requestedDateTime) {
        OpenMeteoResponse.CurrentWeather current = response.getCurrent();

        String temperature = String.format(Locale.US, "%.1f°C", current.getTemperature2m());
        String precipitation = String.format(Locale.US, "%.1fmm", current.getPrecipitation());
        String windSpeed = String.format(Locale.US, "%.1f km/h", current.getWindSpeed10m());

        log.debug("Weather formatted for {} - Temp: {}, Precip: {}, Wind: {}",
                requestedDateTime, temperature, precipitation, windSpeed);

        return new WeatherDTO(temperature, precipitation, windSpeed);
    }
}

package com.flightontime.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightontime.api.dto.WeatherDTO;
import com.flightontime.api.entity.Airport;
import com.flightontime.api.integration.weather.WeatherClient;
import com.flightontime.api.integration.weather.dto.OpenMeteoResponse;
import com.flightontime.api.integration.weather.dto.OpenMeteoResponse.CurrentWeather;
import com.flightontime.api.repository.AirportRepository;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void shouldGetWeatherForAirportSuccessfully() {
        // Arrange
        String airportCode = "SBGR";
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 15, 14, 30);

        Airport airport = new Airport();
        airport.setAirportCode(airportCode);
        airport.setAirportName("Guarulhos International Airport");
        airport.setAirportLatitude(-23.4356);
        airport.setAirportLongitude(-46.4731);

        CurrentWeather currentWeather = new CurrentWeather("2026-01-15T14:30:00", 25.5, 2.3, 15.8);
        OpenMeteoResponse weatherResponse = new OpenMeteoResponse(-23.4356, -46.4731, currentWeather, null);

        when(airportRepository.findByAirportCode(airportCode)).thenReturn(airport);
        when(weatherClient.getWeatherForecast(
                airport.getAirportLatitude(),
                airport.getAirportLongitude(),
                dateTime
        )).thenReturn(weatherResponse);

        // Act
        WeatherDTO result = weatherService.getWeatherForAirport(airportCode, dateTime);

        // Assert
        assertNotNull(result);
        assertEquals("25.5°C", result.getTemperature());
        assertEquals("2.3mm", result.getPrecipitation());
        assertEquals("15.8 km/h", result.getWindSpeed());

        verify(airportRepository, times(1)).findByAirportCode(airportCode);
        verify(weatherClient, times(1)).getWeatherForecast(
                airport.getAirportLatitude(),
                airport.getAirportLongitude(),
                dateTime
        );
    }

    @Test
    void shouldHandleZeroWeatherValues() {
        // Arrange
        String airportCode = "SBBR";
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 15, 10, 0);

        Airport airport = new Airport();
        airport.setAirportCode(airportCode);
        airport.setAirportName("Brasília International Airport");
        airport.setAirportLatitude(-15.8697);
        airport.setAirportLongitude(-47.9172);

        CurrentWeather currentWeather = new CurrentWeather("2026-01-15T10:00:00", 0.0, 0.0, 0.0);
        OpenMeteoResponse weatherResponse = new OpenMeteoResponse(-15.8697, -47.9172, currentWeather, null);

        when(airportRepository.findByAirportCode(airportCode)).thenReturn(airport);
        when(weatherClient.getWeatherForecast(anyDouble(), anyDouble(), any()))
                .thenReturn(weatherResponse);

        // Act
        WeatherDTO result = weatherService.getWeatherForAirport(airportCode, dateTime);

        // Assert
        assertNotNull(result);
        assertEquals("0.0°C", result.getTemperature());
        assertEquals("0.0mm", result.getPrecipitation());
        assertEquals("0.0 km/h", result.getWindSpeed());
    }

    @Test
    void shouldFormatWeatherDataCorrectly() {
        // Arrange
        String airportCode = "SBSP";
        LocalDateTime dateTime = LocalDateTime.of(2026, 2, 20, 18, 45);

        Airport airport = new Airport();
        airport.setAirportCode(airportCode);
        airport.setAirportName("Congonhas Airport");
        airport.setAirportLatitude(-23.6261);
        airport.setAirportLongitude(-46.6561);

        // Valores com muitas casas decimais para testar formatação
        CurrentWeather currentWeather = new CurrentWeather("2026-02-20T18:45:00", 28.7654321, 5.123456, 22.987654);
        OpenMeteoResponse weatherResponse = new OpenMeteoResponse(-23.6261, -46.6561, currentWeather, null);

        when(airportRepository.findByAirportCode(airportCode)).thenReturn(airport);
        when(weatherClient.getWeatherForecast(anyDouble(), anyDouble(), any()))
                .thenReturn(weatherResponse);

        // Act
        WeatherDTO result = weatherService.getWeatherForAirport(airportCode, dateTime);

        // Assert
        assertNotNull(result);
        // Verifica se está formatado com 1 casa decimal
        assertEquals("28.8°C", result.getTemperature());
        assertEquals("5.1mm", result.getPrecipitation());
        assertEquals("23.0 km/h", result.getWindSpeed());
    }

    @Test
    void shouldThrowExceptionWhenAirportNotFound() {
        // Arrange
        String airportCode = "INVALID";
        LocalDateTime dateTime = LocalDateTime.now();

        when(airportRepository.findByAirportCode(airportCode)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            weatherService.getWeatherForAirport(airportCode, dateTime);
        });

        verify(weatherClient, never()).getWeatherForecast(anyDouble(), anyDouble(), any());
    }
}

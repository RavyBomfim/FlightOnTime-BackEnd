package com.flightontime.api.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.dto.WeatherDTO;
import com.flightontime.api.entity.Airline;
import com.flightontime.api.entity.Airport;
import com.flightontime.api.entity.Flight;
import com.flightontime.api.integration.prediction.PredictionClient;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;
import com.flightontime.api.integration.weather.WeatherClient;
import com.flightontime.api.integration.weather.dto.OpenMeteoResponse;
import com.flightontime.api.integration.weather.dto.OpenMeteoResponse.CurrentWeather;
import com.flightontime.api.repository.AirlineRepository;
import com.flightontime.api.repository.AirportRepository;
import com.flightontime.api.repository.FlightRepository;
import com.flightontime.api.service.PredictionService;

import java.time.LocalDateTime;

/**
 * Integration test for the complete flight prediction flow
 * Tests the entire pipeline: Controller -> Service -> Repository -> External APIs
 */
@SpringBootTest
@ActiveProfiles("test")
class FlightPredictionIntegrationTest {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private FlightRepository flightRepository;

    @MockBean
    private PredictionClient predictionClient;

    @MockBean
    private WeatherClient weatherClient;

    @BeforeEach
    void setUp() {
        // Clear all data before each test to avoid conflicts
        flightRepository.deleteAll();
        airlineRepository.deleteAll();
        airportRepository.deleteAll();
    }

    @Test
    void shouldCompleteFullPredictionFlowSuccessfully() {
        // Arrange - Setup test data in repositories
        setupTestData();

        // Mock external API responses
        when(predictionClient.predict(any()))
                .thenReturn(new PredictionResponse(true, 0.85));

        CurrentWeather currentWeather = new CurrentWeather("2026-01-15T14:30:00", 25.5, 2.0, 15.0);
        when(weatherClient.getWeatherForecast(anyDouble(), anyDouble(), any()))
                .thenReturn(new OpenMeteoResponse(-23.4356, -46.4731, currentWeather, null));

        FlightRequestDTO request = new FlightRequestDTO(
                "TST",
                "TEST",
                "TST2",
                LocalDateTime.of(2026, 1, 15, 14, 30)
        );

        // Act
        FlightResponseDTO result = predictionService.predict(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.predict());
        assertNotNull(result.weather());

        // Verify prediction
        assertEquals("Atrasado", result.predict().getPredictionResult());
        assertEquals(0.85, result.predict().getPredictionProbability());

        // Verify weather data
        assertEquals("25.5°C", result.weather().getTemperature());
        assertEquals("2.0mm", result.weather().getPrecipitation());
        assertEquals("15.0 km/h", result.weather().getWindSpeed());

        // Verify data was saved to database
        assertTrue(flightRepository.findAll().size() > 0);

        // Verify external APIs were called
        verify(predictionClient, times(1)).predict(any());
        verify(weatherClient, times(1)).getWeatherForecast(anyDouble(), anyDouble(), any());
    }

    @Test
    void shouldCalculateDistanceCorrectlyBetweenAirports() {
        // Arrange
        setupTestData();

        when(predictionClient.predict(any()))
                .thenReturn(new PredictionResponse(false, 0.25));

        CurrentWeather currentWeather = new CurrentWeather("2026-01-15T14:30:00", 20.0, 0.0, 10.0);
        when(weatherClient.getWeatherForecast(anyDouble(), anyDouble(), any()))
                .thenReturn(new OpenMeteoResponse(-23.4356, -46.4731, currentWeather, null));

        FlightRequestDTO request = new FlightRequestDTO(
                "TST",
                "TEST",
                "TST2",
                LocalDateTime.now()
        );

        // Act
        FlightResponseDTO result = predictionService.predict(request);

        // Assert
        assertNotNull(result);
        
        // Verify the distance was calculated (Haversine formula)
        // Distance between (-23.4356, -46.4731) and (-15.8697, -47.9172) should be ~872km
        verify(predictionClient).predict(argThat(predRequest -> {
            // Verify that distance was calculated and is reasonable
            int distance = predRequest.distanceMeters();
            return distance > 800000 && distance < 900000; // ~872km in meters
        }));
    }

    @Test
    void shouldThrowExceptionWhenAirlineNotFound() {
        // Arrange
        FlightRequestDTO request = new FlightRequestDTO(
                "XXX", // Non-existent airline
                "TEST",
                "TST2",
                LocalDateTime.now()
        );

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            predictionService.predict(request);
        });

        // Verify external APIs were not called
        verify(predictionClient, never()).predict(any());
        verify(weatherClient, never()).getWeatherForecast(anyDouble(), anyDouble(), any());
    }

    @Test
    void shouldThrowExceptionWhenOriginAirportNotFound() {
        // Arrange
        setupTestData();

        FlightRequestDTO request = new FlightRequestDTO(
                "TST",
                "XXXX", // Non-existent airport
                "TST2",
                LocalDateTime.now()
        );

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            predictionService.predict(request);
        });

        verify(predictionClient, never()).predict(any());
    }

    @Test
    void shouldThrowExceptionWhenDestinationAirportNotFound() {
        // Arrange
        setupTestData();

        FlightRequestDTO request = new FlightRequestDTO(
                "TST",
                "TEST",
                "XXXX", // Non-existent airport
                LocalDateTime.now()
        );

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            predictionService.predict(request);
        });

        verify(predictionClient, never()).predict(any());
    }

    @Test
    void shouldHandlePunctualPrediction() {
        // Arrange
        setupTestData();

        // Mock prediction for on-time flight
        when(predictionClient.predict(any()))
                .thenReturn(new PredictionResponse(false, 0.15)); // false = Pontual

        CurrentWeather currentWeather = new CurrentWeather("2026-01-15T14:30:00", 22.0, 0.0, 5.0);
        when(weatherClient.getWeatherForecast(anyDouble(), anyDouble(), any()))
                .thenReturn(new OpenMeteoResponse(-23.4356, -46.4731, currentWeather, null));

        FlightRequestDTO request = new FlightRequestDTO(
                "TST",
                "TEST",
                "TST2",
                LocalDateTime.now()
        );

        // Act
        FlightResponseDTO result = predictionService.predict(request);

        // Assert
        assertNotNull(result);
        assertEquals("Pontual", result.predict().getPredictionResult());
        assertEquals(0.15, result.predict().getPredictionProbability());
    }

    private void setupTestData() {
        // Create test airline
        Airline airline = new Airline();
        airline.setAirlineCode("TST");
        airline.setAirlineName("Test Airline");
        airlineRepository.save(airline);

        // Create test origin airport (Guarulhos coordinates)
        Airport origin = new Airport();
        origin.setAirportCode("TEST");
        origin.setAirportName("Test Origin Airport");
        origin.setAirportCity("São Paulo");
        origin.setAirportState("SP");
        origin.setAirportLatitude(-23.4356);
        origin.setAirportLongitude(-46.4731);
        airportRepository.save(origin);

        // Create test destination airport (Brasília coordinates)
        Airport destination = new Airport();
        destination.setAirportCode("TST2");
        destination.setAirportName("Test Destination Airport");
        destination.setAirportCity("Brasília");
        destination.setAirportState("DF");
        destination.setAirportLatitude(-15.8697);
        destination.setAirportLongitude(-47.9172);
        airportRepository.save(destination);
    }
}

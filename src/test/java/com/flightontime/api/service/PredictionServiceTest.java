package com.flightontime.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightontime.api.dto.*;
import com.flightontime.api.entity.*;
import com.flightontime.api.repository.*;
import com.flightontime.api.integration.prediction.PredictionClient;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {

    @Mock private PredictionClient predictionClient;
    @Mock private FlightRepository flightRepository;
    @Mock private WeatherService weatherService;
    @Mock private AirportRepository airportRepository;
    @Mock private AirlineRepository airlineRepository;

    @InjectMocks
    private PredictionService predictionService;

    @Test
    void shouldReturnPredictionResultSuccessfully() {
        // 1. Dados de Entrada (Record)
        var request = new FlightRequestDTO("GOL", "SBGR", "SBBR", LocalDateTime.now());

        // 2. Mocks dos Aeroportos (com coordenadas para o cálculo de Haversine)
        Airport origin = new Airport();
        origin.setAirportCode("SBGR");
        origin.setAirportLatitude(-23.4356); // Guarulhos
        origin.setAirportLongitude(-46.4731);

        Airport destination = new Airport();
        destination.setAirportCode("SBBR");
        destination.setAirportLatitude(-15.8697); // Brasília
        destination.setAirportLongitude(-47.9172);

        when(airportRepository.findByAirportCode("SBGR")).thenReturn(origin);
        when(airportRepository.findByAirportCode("SBBR")).thenReturn(destination);

        // 3. Mock da Companhia Aérea
        when(airlineRepository.findByAirlineCode("GOL")).thenReturn(new Airline());

        // 4. Mock da API Python (PredictionClient)
        when(predictionClient.predict(any())).thenReturn(new PredictionResponse(true, 0.85));

        // 5. Mock do Clima
        when(weatherService.getWeatherForAirport(anyString(), any()))
                .thenReturn(new WeatherDTO("25", "0", "10"));

        // 6. Mock do Salvamento (Evita o erro de savedFlight.getId())
        Flight savedFlight = new Flight();
        savedFlight.setId(100L); 
        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

        // --- EXECUÇÃO ---
        FlightResponseDTO result = predictionService.predict(request);

        // --- VALIDAÇÕES ---
        assertNotNull(result);
        assertEquals("Atrasado", result.predict().getPredictionResult());
        assertEquals(0.85, result.predict().getPredictionProbability());
       // assertEquals("25", result.weather().temperature());

        // Verifica se salvou no banco
        verify(flightRepository, times(1)).save(any(Flight.class));
    }
}
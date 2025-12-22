package com.flightontime.api.service;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.entity.Flight;
import com.flightontime.api.integration.prediction.PredictionClient;
import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;
import com.flightontime.api.repository.FlightRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionClient predictionClient;
    private final FlightRepository flightRepository;

    public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
        log.info("Recebendo requisição de predição: {}", flightRequestDTO);

        PredictionRequest payload = new PredictionRequest(
                flightRequestDTO.companhia(),
                flightRequestDTO.origem(),
                flightRequestDTO.destino(),
                flightRequestDTO.data_partida().toString(),
                flightRequestDTO.distancia_km());

        log.debug("Enviando payload para API Python: {}", payload);
        PredictionResponse response = predictionClient.predict(payload);
        log.info("Predição recebida: {}", response);

        // Salvar no banco de dados
        Flight flight = new Flight();
        flight.setAirline(flightRequestDTO.companhia());
        flight.setOrigin(flightRequestDTO.origem());
        flight.setDestination(flightRequestDTO.destino());
        flight.setDistanceKm(flightRequestDTO.distancia_km().intValue());
        flight.setScheduledDeparture(flightRequestDTO.data_partida());
        flight.setScheduledArrival(flightRequestDTO.data_partida().plusHours(2)); // Estimativa
        flight.setPredictionResult(response.previsao());
        flight.setPredictionProbability(response.probabilidade());

        Flight savedFlight = flightRepository.save(flight);
        log.info("Voo salvo no banco de dados com ID: {}", savedFlight.getId());

        return new FlightResponseDTO(
                response.previsao(),
                response.probabilidade());
    }
}

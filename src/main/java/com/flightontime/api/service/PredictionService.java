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

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionClient predictionClient;
    private final FlightRepository flightRepository;

    public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
        log.info("Recebendo requisição de predição: {}", flightRequestDTO);

        int timeInMinutes = flightRequestDTO.scheduledDepartureTime().getHour() * 60
                + flightRequestDTO.scheduledDepartureTime().getMinute();

        // Obter o dia da semana (1 = Segunda, 7 = Domingo)
        int dayOfWeek = flightRequestDTO.scheduledDepartureDate().getDayOfWeek().getValue();

        PredictionRequest payload = new PredictionRequest(
                flightRequestDTO.airline(),
                flightRequestDTO.origin(),
                flightRequestDTO.destination(),
                flightRequestDTO.scheduledDepartureDate(),
                dayOfWeek,
                timeInMinutes);

        log.debug("Enviando payload para API Python: {}", payload);
        PredictionResponse response = predictionClient.predict(payload);
        log.info("Predição recebida: {}", response);

        // Salvar no banco de dados
        Flight flight = new Flight();
        flight.setAirline(flightRequestDTO.airline());
        flight.setOrigin(flightRequestDTO.origin());
        flight.setDestination(flightRequestDTO.destination());
        flight.setScheduledDepartureDate(flightRequestDTO.scheduledDepartureDate());
        flight.setDayOfWeek(dayOfWeek);
        flight.setScheduledDepartureTimeInMinutes(timeInMinutes);
        flight.setPredictionResult(String.valueOf(response.predictionResult()));
        flight.setPredictionProbability(response.predictionProbability());

        Flight savedFlight = flightRepository.save(flight);
        log.info("Voo salvo no banco de dados com ID: {}", savedFlight.getId());

        return new FlightResponseDTO(
                response.predictionResult(),
                response.predictionProbability());
    }
}

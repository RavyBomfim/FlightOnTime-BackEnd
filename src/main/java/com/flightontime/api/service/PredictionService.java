package com.flightontime.api.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.entity.Flight;
import com.flightontime.api.integration.prediction.PredictionClient;
import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;
import com.flightontime.api.repository.FlightRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

        private final PredictionClient predictionClient;
        private final FlightRepository flightRepository;

        @CacheEvict(value = "flightStats", allEntries = true)
        public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
                log.info("Recebendo requisição de predição: {}", flightRequestDTO);

                // Calcular horário em minutos desde meia-noite
                int timeInMinutes = flightRequestDTO.scheduledDeparture().getHour() * 60
                                + flightRequestDTO.scheduledDeparture().getMinute();

                // Obter o dia da semana (1 = Segunda, 7 = Domingo)
                int dayOfWeek = flightRequestDTO.scheduledDeparture().getDayOfWeek().getValue();

                PredictionRequest payload = new PredictionRequest(
                                flightRequestDTO.airline(),
                                flightRequestDTO.origin(),
                                flightRequestDTO.destination(),
                                flightRequestDTO.scheduledDeparture().toLocalDate(),
                                dayOfWeek,
                                timeInMinutes);

                log.debug("Enviando payload para API Python: {}", payload);
                PredictionResponse response = predictionClient.predict(payload);
                log.info("Predição recebida: {}", response);
                String predictionResultText = response.predictionResult() ? "Atrasado" : "Pontual";

                // Salvar no banco de dados
                Flight flight = new Flight();
                flight.setAirline(flightRequestDTO.airline());
                flight.setOrigin(flightRequestDTO.origin());
                flight.setDestination(flightRequestDTO.destination());
                flight.setScheduledDepartureDate(flightRequestDTO.scheduledDeparture().toLocalDate());
                flight.setDayOfWeek(dayOfWeek);
                flight.setScheduledDepartureTimeInMinutes(timeInMinutes);
                flight.setPredictionResult(predictionResultText);
                flight.setPredictionProbability(response.predictionProbability());

                Flight savedFlight = flightRepository.save(flight);
                log.info("Voo salvo no banco de dados com ID: {}", savedFlight.getId());

                return new FlightResponseDTO(
                                response.predictionResult(),
                                response.predictionProbability());
        }
}

package com.flightontime.api.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.dto.PredictionDTO;
import com.flightontime.api.dto.WeatherDTO;
import com.flightontime.api.entity.Airline;
import com.flightontime.api.entity.Airport;
import com.flightontime.api.entity.Flight;
import com.flightontime.api.integration.prediction.PredictionClient;
import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;
import com.flightontime.api.repository.AirlineRepository;
import com.flightontime.api.repository.AirportRepository;
import com.flightontime.api.repository.FlightRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

        private final PredictionClient predictionClient;
        private final FlightRepository flightRepository;
        private final WeatherService weatherService;
        private final AirportRepository airportRepository;
        private final AirlineRepository airlineRepository;

        @CacheEvict(value = "flightStats", allEntries = true)
        public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
                log.info("Recebendo requisição de predição: {}", flightRequestDTO);

                // Validar aeroportos de origem e destino
                Airport originAirport = airportRepository.findByAirportCode(flightRequestDTO.origin());
                if (originAirport == null) {
                        log.error("Aeroporto de origem não encontrado: {}", flightRequestDTO.origin());
                        throw new RuntimeException("Aeroporto de origem não encontrado: " + flightRequestDTO.origin());
                }

                Airport destinationAirport = airportRepository.findByAirportCode(flightRequestDTO.destination());
                if (destinationAirport == null) {
                        log.error("Aeroporto de destino não encontrado: {}", flightRequestDTO.destination());
                        throw new RuntimeException("Aeroporto de destino não encontrado: " + flightRequestDTO.destination());
                }

                Airline airlineCheck = airlineRepository.findByAirlineCode(flightRequestDTO.airline());
                if (airlineCheck == null) {
                        log.error("Companhia aérea inválida: {}", flightRequestDTO.airline());
                        throw new RuntimeException("Companhia aérea inválida: " + flightRequestDTO.airline());
                }

                log.debug("Aeroportos validados - Origem: {}, Destino: {}",
                                originAirport.getAirportName(), destinationAirport.getAirportName());

                // Calcular distância em quilômetros usando a fórmula de Haversine
                double distanceKmDouble = calculateDistanceKm(originAirport, destinationAirport);
                int distanceKm = (int) Math.round(distanceKmDouble);
                log.debug("Distância calculada entre aeroportos: {} km", distanceKm);

                // Obter o dia da semana (1 = Segunda, 7 = Domingo)
                int dayOfWeek = flightRequestDTO.scheduledDeparture().getDayOfWeek().getValue();

                PredictionRequest payload = new PredictionRequest(
                                flightRequestDTO.airline(),
                                flightRequestDTO.origin(),
                                flightRequestDTO.destination(),
                                flightRequestDTO.scheduledDeparture().toLocalDate(),
                                dayOfWeek,
                                distanceKm);

                log.debug("Enviando payload para API Python: {}", payload);
                PredictionResponse response = predictionClient.predict(payload);
                log.info("Predição recebida: {}", response);
                String predictionResultText = response.predictionResult() ? "Atrasado" : "Pontual";

                // Buscar dados meteorológicos para o aeroporto de origem na data/hora do voo
                log.debug("Buscando dados meteorológicos para aeroporto: {} em {}",
                                flightRequestDTO.origin(), flightRequestDTO.scheduledDeparture());
                WeatherDTO weather = weatherService.getWeatherForAirport(
                                flightRequestDTO.origin(),
                                flightRequestDTO.scheduledDeparture());

                // Salvar no banco de dados
                Flight flight = new Flight();
                flight.setAirline(flightRequestDTO.airline());
                flight.setOrigin(flightRequestDTO.origin());
                flight.setDestination(flightRequestDTO.destination());
                flight.setScheduledDepartureDate(flightRequestDTO.scheduledDeparture().toLocalDate());
                flight.setDayOfWeek(dayOfWeek);
                flight.setDistanceKm(distanceKm);
                flight.setPredictionResult(predictionResultText);
                flight.setPredictionProbability(response.predictionProbability());

                Flight savedFlight = flightRepository.save(flight);
                log.info("Voo salvo no banco de dados com ID: {}", savedFlight.getId());

                PredictionDTO predictionDTO = new PredictionDTO(
                                response.predictionResult(),
                                response.predictionProbability());

                return new FlightResponseDTO(predictionDTO, weather);
        }

        private double calculateDistanceKm(Airport origin, Airport destination) {
                final int EARTH_RADIUS_KM = 6371;
                
                double latDistance = Math.toRadians(destination.getAirportLatitude() - origin.getAirportLatitude());
                double lonDistance = Math.toRadians(destination.getAirportLongitude() - origin.getAirportLongitude());
                
                double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                                + Math.cos(Math.toRadians(origin.getAirportLatitude()))
                                * Math.cos(Math.toRadians(destination.getAirportLatitude()))
                                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                
                return EARTH_RADIUS_KM * c;
        }
}

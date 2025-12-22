package com.flightontime.api.service;

import com.flightontime.api.entity.Flight;
import com.flightontime.api.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAllByOrderByCreatedAtDesc();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado com ID: " + id));
    }

    public List<Flight> getFlightsByOrigin(String origin) {
        return flightRepository.findByOrigin(origin);
    }

    public List<Flight> getFlightsByDestination(String destination) {
        return flightRepository.findByDestination(destination);
    }

    public List<Flight> getFlightsByPredictionResult(String status) {
        String result = status.substring(0, 1).toUpperCase(Locale.ROOT) + status.substring(1).toLowerCase(Locale.ROOT);
        return flightRepository.findByPredictionResult(result);
    }

    public List<Flight> getFlightsByRoute(String origin, String destination) {
        return flightRepository.findByOriginAndDestination(origin, destination);
    }

    public List<Flight> getDelayedFlights() {
        return flightRepository.findDelayedFlights();
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }
}

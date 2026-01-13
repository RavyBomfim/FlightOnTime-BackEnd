package com.flightontime.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightontime.api.dto.FlightStatsDTO;
import com.flightontime.api.dto.OverallStats;
import com.flightontime.api.dto.StatsByAirline;
import com.flightontime.api.dto.StatsByDate;
import com.flightontime.api.dto.StatsByDestination;
import com.flightontime.api.dto.StatsByOrigin;
import com.flightontime.api.dto.StatsByRoute;
import com.flightontime.api.entity.Flight;
import com.flightontime.api.repository.FlightRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

        private final FlightRepository flightRepository;

        public List<Flight> getAllFlights() {
                return flightRepository.findAllByOrderByCreatedAtDesc();
        }

        @Transactional(readOnly = true)
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

        public List<Flight> getOntimeFlights() {
                return flightRepository.findOntimeFlights();
        }

        public List<Flight> getFlightsByRoute(String origin, String destination) {
                return flightRepository.findByOriginAndDestination(origin, destination);
        }

        public List<Flight> getDelayedFlights() {
                return flightRepository.findDelayedFlights();
        }

        @CacheEvict(value = "flightStats", allEntries = true)
        public void deleteFlight(Long id) {
                log.debug("Deleting flight with ID: {} and invalidating stats cache", id);
                flightRepository.deleteById(id);
        }

        /**
         * Calculates comprehensive flight statistics using database aggregation
         * Optimized to avoid loading all flights into memory
         * 
         * @return FlightStatsDTO with all statistics
         */
        @Cacheable(value = "flightStats", unless = "#result == null")
        @Transactional(readOnly = true)
        public FlightStatsDTO getFlightStats() {
                log.debug("Calculating flight statistics using optimized queries");

                // Calculate overall stats using simple count queries
                long totalFlights = flightRepository.countAllFlights();
                long delayedFlights = flightRepository.countByStatus("Atrasado");
                long ontimeFlights = flightRepository.countByStatus("Pontual");
                double delayPercentage = totalFlights > 0 ? (delayedFlights * 100.0 / totalFlights) : 0.0;

                OverallStats overallStats = new OverallStats(
                                totalFlights, delayedFlights, ontimeFlights, delayPercentage);

                // Calculate stats by date using database aggregation
                List<StatsByDate> statsByDate = flightRepository
                                .findStatsGroupedByDate("Atrasado")
                                .stream()
                                .map(result -> {
                                        java.sql.Date sqlDate = (java.sql.Date) result[0];
                                        LocalDate dateOnly = sqlDate.toLocalDate();
                                        LocalDateTime date = dateOnly.atStartOfDay();
                                        long total = ((Number) result[1]).longValue();
                                        long delayed = ((Number) result[2]).longValue();
                                        double percentage = total > 0 ? (delayed * 100.0 / total) : 0.0;
                                        return new StatsByDate(date, total, delayed, percentage);
                                })
                                .collect(Collectors.toList());

                // Calculate stats by airline using database aggregation
                List<StatsByAirline> statsByAirline = flightRepository
                                .findStatsGroupedByAirline("Atrasado")
                                .stream()
                                .map(result -> {
                                        String airline = (String) result[0];
                                        long total = ((Number) result[1]).longValue();
                                        long delayed = ((Number) result[2]).longValue();
                                        double percentage = total > 0 ? (delayed * 100.0 / total) : 0.0;
                                        return new StatsByAirline(airline, total, delayed, percentage);
                                })
                                .collect(Collectors.toList());

                // Calculate stats by origin using database aggregation
                List<StatsByOrigin> statsByOrigin = flightRepository
                                .findStatsGroupedByOrigin("Atrasado")
                                .stream()
                                .map(result -> {
                                        String origin = (String) result[0];
                                        long total = ((Number) result[1]).longValue();
                                        long delayed = ((Number) result[2]).longValue();
                                        double percentage = total > 0 ? (delayed * 100.0 / total) : 0.0;
                                        return new StatsByOrigin(origin, total, delayed, percentage);
                                })
                                .collect(Collectors.toList());

                // Calculate stats by destination using database aggregation
                List<StatsByDestination> statsByDestination = flightRepository
                                .findStatsGroupedByDestination("Atrasado")
                                .stream()
                                .map(result -> {
                                        String destination = (String) result[0];
                                        long total = ((Number) result[1]).longValue();
                                        long delayed = ((Number) result[2]).longValue();
                                        double percentage = total > 0 ? (delayed * 100.0 / total) : 0.0;
                                        return new StatsByDestination(destination, total, delayed, percentage);
                                })
                                .collect(Collectors.toList());

                // Calculate stats by route using database aggregation
                List<StatsByRoute> statsByRoute = flightRepository
                                .findStatsGroupedByRoute("Atrasado")
                                .stream()
                                .map(result -> {
                                        String origin = (String) result[0];
                                        String destination = (String) result[1];
                                        long total = ((Number) result[2]).longValue();
                                        long delayed = ((Number) result[3]).longValue();
                                        double percentage = total > 0 ? (delayed * 100.0 / total) : 0.0;
                                        return new StatsByRoute(origin, destination, total, delayed, percentage);
                                })
                                .collect(Collectors.toList());

                log.debug("Flight statistics calculated successfully. Total flights: {}", totalFlights);

                return new FlightStatsDTO(
                                overallStats,
                                statsByDate,
                                statsByAirline,
                                statsByOrigin,
                                statsByDestination,
                                statsByRoute);
        }
}

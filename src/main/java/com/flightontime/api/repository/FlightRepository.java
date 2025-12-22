package com.flightontime.api.repository;

import com.flightontime.api.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByOrderByCreatedAtDesc();

    List<Flight> findByOrigin(String origin);

    List<Flight> findByDestination(String destination);

    List<Flight> findByPredictionResult(String predictionResult);

    // @Query("SELECT f FROM Flight f WHERE f.scheduledDeparture BETWEEN :start AND
    // :end")
    // List<Flight> findFlightsByDepartureDateRange(LocalDateTime start,
    // LocalDateTime end);

    @Query("SELECT f FROM Flight f WHERE f.origin = :origin AND f.destination = :destination")
    List<Flight> findByOriginAndDestination(String origin, String destination);

    @Query("SELECT f FROM Flight f WHERE f.predictionResult = 'Atraso' ORDER BY f.createdAt DESC")
    List<Flight> findDelayedFlights();
}

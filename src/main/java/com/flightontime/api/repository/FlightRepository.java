package com.flightontime.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flightontime.api.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByOrderByCreatedAtDesc();

    List<Flight> findByOrigin(String origin);

    List<Flight> findByDestination(String destination);

    List<Flight> findByPredictionResult(String predictionResult);

    @Query("SELECT f FROM Flight f WHERE f.origin = :origin AND f.destination = :destination")
    List<Flight> findByOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination);

    @Query("SELECT f FROM Flight f WHERE f.predictionResult = :status ORDER BY f.createdAt DESC")
    List<Flight> findByPredictionResultOrdered(@Param("status") String status);

    @Query("SELECT f FROM Flight f WHERE f.predictionResult = 'Atrasado' ORDER BY f.createdAt DESC")
    List<Flight> findDelayedFlights();

    @Query("SELECT f FROM Flight f WHERE f.predictionResult = 'Pontual' ORDER BY f.createdAt DESC")
    List<Flight> findOntimeFlights();

    // Statistics queries - Basic counts
    @Query("SELECT COUNT(f) FROM Flight f")
    Long countAllFlights();

    @Query("SELECT COUNT(f) FROM Flight f WHERE f.predictionResult = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(f) FROM Flight f WHERE f.predictionResult = 'Atrasado'")
    Long countDelayedFlights();

    @Query("SELECT COUNT(f) FROM Flight f WHERE f.predictionResult = 'Pontual'")
    Long countOntimeFlights();

    // Optimized queries for statistics aggregation - Returns Object[] with [field, total, delayed]
    @Query("SELECT CAST(f.scheduledDepartureDate AS date), COUNT(f), " +
           "SUM(CASE WHEN f.predictionResult = :delayedStatus THEN 1 ELSE 0 END) " +
           "FROM Flight f " +
           "GROUP BY CAST(f.scheduledDepartureDate AS date) " +
           "ORDER BY CAST(f.scheduledDepartureDate AS date) DESC")
    List<Object[]> findStatsGroupedByDate(@Param("delayedStatus") String delayedStatus);

    @Query("SELECT f.airline, COUNT(f), " +
           "SUM(CASE WHEN f.predictionResult = :delayedStatus THEN 1 ELSE 0 END) " +
           "FROM Flight f " +
           "GROUP BY f.airline " +
           "ORDER BY COUNT(f) DESC")
    List<Object[]> findStatsGroupedByAirline(@Param("delayedStatus") String delayedStatus);

    @Query("SELECT f.origin, COUNT(f), " +
           "SUM(CASE WHEN f.predictionResult = :delayedStatus THEN 1 ELSE 0 END) " +
           "FROM Flight f " +
           "GROUP BY f.origin " +
           "ORDER BY COUNT(f) DESC")
    List<Object[]> findStatsGroupedByOrigin(@Param("delayedStatus") String delayedStatus);

    @Query("SELECT f.destination, COUNT(f), " +
           "SUM(CASE WHEN f.predictionResult = :delayedStatus THEN 1 ELSE 0 END) " +
           "FROM Flight f " +
           "GROUP BY f.destination " +
           "ORDER BY COUNT(f) DESC")
    List<Object[]> findStatsGroupedByDestination(@Param("delayedStatus") String delayedStatus);

    @Query("SELECT f.origin, f.destination, COUNT(f), " +
           "SUM(CASE WHEN f.predictionResult = :delayedStatus THEN 1 ELSE 0 END) " +
           "FROM Flight f " +
           "GROUP BY f.origin, f.destination " +
           "ORDER BY COUNT(f) DESC")
    List<Object[]> findStatsGroupedByRoute(@Param("delayedStatus") String delayedStatus);
}

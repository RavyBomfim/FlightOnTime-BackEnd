package com.flightontime.api.repository;

import com.flightontime.api.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Airport findByAirportCode(String airportCode);
}

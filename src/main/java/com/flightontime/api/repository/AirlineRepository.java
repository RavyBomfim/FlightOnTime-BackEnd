package com.flightontime.api.repository;

import com.flightontime.api.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Airline findByAirlineCode(String airlineCode);
}

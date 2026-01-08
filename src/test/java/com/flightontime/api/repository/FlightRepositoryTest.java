package com.flightontime.api.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.flightontime.api.entity.Flight;

@DataJpaTest
class FlightRepositoryTest {

    @Autowired
    private FlightRepository repository;

    @Test
    void shouldFindFlightsByOrigin() {
        List<Flight> flights = repository.findByOrigin("GRU");
        assertNotNull(flights);
    }
}

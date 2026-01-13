package com.flightontime.api.service;

import com.flightontime.api.entity.Airline;
import com.flightontime.api.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    public Airline getAirlineById(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airline not found with id: " + id));
    }
}

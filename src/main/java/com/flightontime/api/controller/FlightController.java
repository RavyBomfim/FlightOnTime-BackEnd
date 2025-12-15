package com.flightontime.api.controller;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.service.PredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final PredictionService service;

    @PostMapping("/predict")
    public ResponseEntity<FlightResponseDTO> predict(@RequestBody @Valid FlightRequestDTO request) {
        var response = service.predict(request);
        return ResponseEntity.ok(response);
    }

}

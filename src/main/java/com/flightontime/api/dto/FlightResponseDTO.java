package com.flightontime.api.dto;

public record FlightResponseDTO(
        boolean predictionResult,
        Double predictionProbability) {
}
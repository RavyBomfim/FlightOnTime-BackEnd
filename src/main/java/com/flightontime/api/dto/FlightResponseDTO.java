package com.flightontime.api.dto;

public record FlightResponseDTO(
        PredictionDTO predict,
        WeatherDTO weather) {
}
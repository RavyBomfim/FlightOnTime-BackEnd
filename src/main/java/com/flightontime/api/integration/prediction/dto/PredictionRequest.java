package com.flightontime.api.integration.prediction.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictionRequest(
        @JsonProperty("companhia") String airline,
        @JsonProperty("origem") String origin,
        @JsonProperty("destino") String destination,
        @JsonProperty("data_partida") LocalDate scheduledDepartureDate,
        @JsonProperty("distancia_metros") int distanceMeters) {
}

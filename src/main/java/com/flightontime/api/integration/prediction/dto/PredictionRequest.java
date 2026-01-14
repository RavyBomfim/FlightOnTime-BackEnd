package com.flightontime.api.integration.prediction.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictionRequest(
        @JsonProperty("companhia") String airline,
        @JsonProperty("origem") String origin,
        @JsonProperty("destino") String destination,
        @JsonProperty("data_partida") 
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime scheduledDepartureDate,
        @JsonProperty("distancia_m") int distanceMeters) {
}

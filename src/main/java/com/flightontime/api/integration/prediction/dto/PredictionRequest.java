package com.flightontime.api.integration.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record PredictionRequest(
        @JsonProperty("companhia") String airline,
        @JsonProperty("origem") String origin,
        @JsonProperty("destino") String destination,
        @JsonProperty("data_partida") LocalDate scheduledDepartureDate,
        @JsonProperty("dia_semana") int dayOfWeek,
        @JsonProperty("horario_minutos") int scheduledDepartureTimeInMinutes) {
}

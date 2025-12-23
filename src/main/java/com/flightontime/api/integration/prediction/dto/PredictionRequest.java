package com.flightontime.api.integration.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;


public record PredictionRequest(
    String airline,
    String origin,
    String destination,
    @JsonProperty("data_partida") LocalDate scheduledDepartureDate,
    int dayOfWeek,
    int scheduledDepartureTimeInMinutes
){}

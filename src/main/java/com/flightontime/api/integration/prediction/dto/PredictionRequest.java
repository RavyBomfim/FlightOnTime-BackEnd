package com.flightontime.api.integration.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PredictionRequest(
    String companhia,
    String origem,
    String destino,
    @JsonProperty("data_partida") String dataPartida,
    @JsonProperty("distancia_km") Double distancia_km

){}

package com.flightontime.api.integration.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictionResponse(
    @JsonProperty("previsao")String previsao,
    double probabilidade
) {

}

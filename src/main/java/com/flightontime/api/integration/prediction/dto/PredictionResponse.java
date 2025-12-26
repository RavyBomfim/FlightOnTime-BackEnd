package com.flightontime.api.integration.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictionResponse(
        @JsonProperty("previsao") boolean predictionResult,
        @JsonProperty("probabilidade") double predictionProbability) {

}

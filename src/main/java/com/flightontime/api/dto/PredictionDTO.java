package com.flightontime.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionDTO {
    private String predictionResult;
    private Double predictionProbability;
}

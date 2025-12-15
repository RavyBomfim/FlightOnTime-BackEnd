package com.flightontime.api.integration.prediction;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PredictionClient {

        private final RestClient pythonApiClient;

        public PredictionResponse predict(PredictionRequest request) {
                return pythonApiClient.post()
                                .uri("/predict")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(request)
                                .retrieve()
                                .body(PredictionResponse.class);
        }
}
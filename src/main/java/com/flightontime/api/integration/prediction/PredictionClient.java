package com.flightontime.api.integration.prediction;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PredictionClient {

        private final RestClient pythonApiClient;

        public PredictionResponse predict(PredictionRequest request) {
                try {
                        return pythonApiClient.post()
                                        .uri("/predict")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(request)
                                        .retrieve()
                                        .onStatus(status -> status.is4xxClientError(),
                                                        (req, res) -> {
                                                                throw new RuntimeException("Erro na requisição: "
                                                                                + res.getStatusText());
                                                        })
                                        .onStatus(status -> status.is5xxServerError(),
                                                        (req, res) -> {
                                                                throw new RuntimeException(
                                                                                "Erro no servidor de predição: "
                                                                                                + res.getStatusText());
                                                        })
                                        .body(PredictionResponse.class);
                } catch (Exception e) {
                        throw new RuntimeException("Falha ao comunicar com a API de predição: " + e.getMessage(), e);
                }
        }
}

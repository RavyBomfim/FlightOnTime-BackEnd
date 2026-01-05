package com.flightontime.api.integration.prediction;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;
import com.flightontime.api.integration.prediction.exception.PredictionServiceException;

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
                                                                log.error("Erro 4xx ao chamar a API de predição");
                                                                throw new PredictionServiceException(
                                        "Dados inválidos para predição");})
                                        .onStatus(status -> status.is5xxServerError(),
                                                        (req, res) -> {
                                                                log.error("Erro 5xx na API de predição");
                                                                throw new PredictionServiceException(
                                                                                "Serviço de predição indisponível");
                                                        })
                                        .body(PredictionResponse.class);

                } catch (ResourceAccessException e) {
                        log.error("API de predição fora do ar", e);
                        throw new PredictionServiceException(
                                        "Não foi possível conectar ao serviço de predição");

                } catch (Exception e) {
                        log.error("Erro inesperado ao chamar serviço de predição", e);
                        throw new PredictionServiceException(
                                        "Erro ao processar a predição");
                }
        }
}

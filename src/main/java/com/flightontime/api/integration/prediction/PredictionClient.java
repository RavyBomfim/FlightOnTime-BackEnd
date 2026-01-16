package com.flightontime.api.integration.prediction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;
import com.flightontime.api.integration.prediction.exception.PredictionServiceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PredictionClient {

        private final RestTemplate restTemplate;
        
        @Value("${python.api.url:http://localhost:8000}")
        private String pythonApiUrl;

        public PredictionResponse predict(PredictionRequest request) {
                try {
                        log.info("Enviando requisição para API de predição: {}", request);
                        
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.set("Authorization", "59f3336ed2b51d151bc6159926347cb0ffa10f8cef42f231de36d687eb459a72");
                        
                        HttpEntity<PredictionRequest> entity = new HttpEntity<>(request, headers);
                        
                        log.info("Enviando para URL: {}/predict", pythonApiUrl);
                        
                        ResponseEntity<PredictionResponse> response = restTemplate.postForEntity(
                                pythonApiUrl + "/predict",
                                entity,
                                PredictionResponse.class
                        );
                        
                        log.info("Predição recebida com sucesso: {}", response.getBody());
                        return response.getBody();

                } catch (Exception e) {
                        log.error("Erro ao chamar serviço de predição", e);
                        throw new PredictionServiceException(
                                        "Erro ao processar a predição: " + e.getMessage());
                }
        }
}

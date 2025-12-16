package com.flightontime.api.service;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.integration.prediction.PredictionClient;

import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor

public class PredictionService {

    private final PredictionClient predictionClient;

    public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {
        log.info("Recebendo requisição de predição: {}", flightRequestDTO);

        PredictionRequest payload = new PredictionRequest(
                flightRequestDTO.companhia(),
                flightRequestDTO.origem(),
                flightRequestDTO.destino(),
                flightRequestDTO.data_partida().toString(),
                flightRequestDTO.distancia_km()
            );

        log.debug("Enviando payload para API Python: {}", payload);
        PredictionResponse response = predictionClient.predict(payload);
        log.info("Predição recebida: {}", response);
            
            
            return new FlightResponseDTO(
                response.previsao(),
                response.probabilidade()
                );

            
    }
}

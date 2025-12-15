package com.flightontime.api.service;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.integration.prediction.PredictionClient;

import com.flightontime.api.integration.prediction.dto.PredictionRequest;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor

public class PredictionService {

    private final PredictionClient predictionClient;

    public FlightResponseDTO predict(FlightRequestDTO flightRequestDTO) {

        // todo substituir o Map pelo FlightRequestDTO record
        PredictionRequest payload = new PredictionRequest(
                flightRequestDTO.companhia(),
                flightRequestDTO.origem(),
                flightRequestDTO.destino(),
                flightRequestDTO.data_partida().toString(),
                flightRequestDTO.distancia_km()
            );

        
        PredictionResponse response = predictionClient.predict(payload);
            
            
            return new FlightResponseDTO(
                response.previsao(),
                response.probabilidade()
                );

            
    }
}

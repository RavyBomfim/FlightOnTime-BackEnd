package com.flightontime.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightontime.api.dto.FlightRequestDTO;
import com.flightontime.api.dto.PredictionDTO;
import com.flightontime.api.integration.prediction.PredictionClient;
import com.flightontime.api.integration.prediction.dto.PredictionResponse;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {

    @Mock
    private PredictionClient predictionClient;

    @InjectMocks
    private PredictionService predictionService;

    @Test
    void shouldReturnPredictionResult() {

        var clientResponse = new PredictionResponse(
                true,
                0.85
        );

        when(predictionClient.predict(any()))
                .thenReturn(clientResponse);

        var result = predictionService.predict(mock(FlightRequestDTO.class));

        assertNotNull(result);
        assertEquals(0.85, result.predict().getPredictionProbability());
        assertEquals("Atrasado", result.predict().getPredictionResult());
    }
}

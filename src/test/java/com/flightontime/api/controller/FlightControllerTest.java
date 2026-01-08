package com.flightontime.api.controller;

import com.flightontime.api.config.TestSecurityConfig;
import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.dto.PredictionDTO;
import com.flightontime.api.dto.WeatherDTO;
import com.flightontime.api.security.jwt.JwtService;
import com.flightontime.api.service.FlightService;
import com.flightontime.api.service.PredictionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
@Import(TestSecurityConfig.class)
class FlightControllerTest {

@Autowired
private MockMvc mockMvc;

@MockBean
private PredictionService predictionService;

@MockBean
private FlightService flightService;

@MockBean
private JwtService jwtService;

    @Test
    void shouldPredictFlightSuccessfully() throws Exception {

        var prediction = new PredictionDTO("Atrasado", 0.78);
        var weather = new WeatherDTO("25", "0", "10");

        when(predictionService.predict(any()))
                .thenReturn(new FlightResponseDTO(prediction, weather));

        mockMvc.perform(post("/api/flights/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "airline": "GOL",
                                  "origin": "SBGR",
                                  "destination": "SBBR",
                                  "scheduledDeparture": "2026-01-15T14:30:00"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.predict.predictionResult").value("Atrasado"))
                .andExpect(jsonPath("$.predict.predictionProbability").value(0.78));
    }
}

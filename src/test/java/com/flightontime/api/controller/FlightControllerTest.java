package com.flightontime.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.flightontime.api.dto.FlightResponseDTO;
import com.flightontime.api.dto.PredictionDTO;
import com.flightontime.api.dto.WeatherDTO;
import com.flightontime.api.service.PredictionService;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

@Autowired
private MockMvc mockMvc;

@MockBean
private PredictionService predictionService;

@Test
@WithMockUser
void shouldPredictFlightSuccessfully() throws Exception {

PredictionDTO prediction = new PredictionDTO(
        "Atrasado",
        0.78
);

WeatherDTO weather = new WeatherDTO(
        "25",
        "0",
        "10"
);

when(predictionService.predict(any()))
        .thenReturn(new FlightResponseDTO(
                prediction,
                weather
        ));

mockMvc.perform(post("/api/flights/predict")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
                {
                "airline": "GOL",
                "originIcao": "SBGR",
                "destinationIcao": "SBBR",
                "scheduledDeparture": "2026-01-15T14:30:00"
                }
        """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.predict.predictionProbability").value(0.78));
}
}

package com.flightontime.api.security;

import com.flightontime.api.controller.FlightController;
import com.flightontime.api.security.jwt.JwtService;
import com.flightontime.api.service.FlightService;
import com.flightontime.api.service.PredictionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class) // Foca apenas no controller de voos
class JwtSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    // Precisamos mockar todas as dependências do Controller para o contexto subir
    @MockBean
    private FlightService flightService;

    @MockBean
    private PredictionService predictionService;

    // Precisamos mockar o JwtService porque seu JwtAuthenticationFilter depende dele
    @MockBean
    private JwtService jwtService;

    @Test
    void shouldReturn401WhenAccessProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isUnauthorized());
    }
}
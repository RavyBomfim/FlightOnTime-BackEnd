package com.flightontime.api.controller;

import com.flightontime.api.dto.auth.LoginRequestDTO;
import com.flightontime.api.dto.auth.LoginResponseDTO;
import com.flightontime.api.service.IAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private IAuthService authService;

    @BeforeEach
    void setup() {
        authService = Mockito.mock(IAuthService.class);
        AuthController controller = new AuthController(authService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldLoginAndReturnJwtToken() throws Exception {

        Mockito.when(authService.login(any(LoginRequestDTO.class)))
                .thenReturn(new LoginResponseDTO("fake-jwt-token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "teste@email.com",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("fake-jwt-token"));
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        Mockito.when(authService.register(any(LoginRequestDTO.class)))
                .thenReturn("Usuário criado com sucesso");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "novo@email.com",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("Usuário criado com sucesso"));
    }
}

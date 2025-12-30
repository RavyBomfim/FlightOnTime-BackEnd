package com.flightontime.api.service;

import com.flightontime.api.dto.auth.LoginRequestDTO;
import com.flightontime.api.dto.auth.LoginResponseDTO;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}


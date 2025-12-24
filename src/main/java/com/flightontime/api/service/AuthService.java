package com.flightontime.api.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flightontime.api.domain.UserEntity;
import com.flightontime.api.dto.auth.LoginRequestDTO;
import com.flightontime.api.dto.auth.LoginResponseDTO;
import com.flightontime.api.repository.UserRepository;
import com.flightontime.api.security.jwt.JwtService;

@Service
@RequiredArgsConstructor 
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return new LoginResponseDTO(jwtService.generateToken(user));
    }
}
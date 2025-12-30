package com.flightontime.api.service;

import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.flightontime.api.domain.UserEntity;
import com.flightontime.api.dto.auth.LoginRequestDTO;
import com.flightontime.api.dto.auth.LoginResponseDTO;
import com.flightontime.api.repository.UserRepository;
import com.flightontime.api.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        UserEntity user = userRepository.findByEmail(dto.email())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado")
            );

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        return new LoginResponseDTO(
            jwtService.generateToken(user)
        );
    }
}

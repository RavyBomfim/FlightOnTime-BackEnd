package com.flightontime.api.service;

import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.flightontime.api.domain.Role;
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
    public LoginResponseDTO login(LoginRequestDTO request) {

        UserEntity user = userRepository.findByEmail(request.email())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")
            );

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        return new LoginResponseDTO(
            jwtService.generateToken(user)
        );
    }

    @Override
    public String register(LoginRequestDTO request) {
        // Verifica se já existe
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado!");
        }
        
        UserEntity user = UserEntity.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
            .build();
        
        userRepository.save(user);
        return "Usuário registrado com sucesso!";
    }
}

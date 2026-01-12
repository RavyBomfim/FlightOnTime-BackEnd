package com.flightontime.api.service;

import com.flightontime.api.domain.Role;
import com.flightontime.api.domain.UserEntity;
import com.flightontime.api.dto.auth.LoginRequestDTO;
import com.flightontime.api.dto.auth.LoginResponseDTO;
import com.flightontime.api.repository.UserRepository;
import com.flightontime.api.security.jwt.JwtService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${google.client-id}")
    private String googleClientId;

    // LOGIN LOCAL
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        UserEntity user = userRepository.findByEmail(request.email())
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário ou senha inválidos"
                )
            );

        if (user.getPassword() == null ||
            !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Usuário ou senha inválidos"
            );
        }

        return new LoginResponseDTO(
            jwtService.generateToken(user)
        );
    }

    // REGISTER LOCAL
    @Override
    public String register(LoginRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email já cadastrado!"
            );
        }

        UserEntity user = UserEntity.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return "Usuário registrado com sucesso!";
    }

    // LOGIN COM GOOGLE
    @Override
    public LoginResponseDTO loginComGoogle(String token) {

        GoogleIdToken.Payload payload = validarTokenGoogle(token);

        String email = payload.getEmail();
        String googleId = payload.getSubject();

        UserEntity user = userRepository
            .findByEmail(email)
            .orElseGet(() -> criarUsuarioGoogle(email, googleId));

        return new LoginResponseDTO(
            jwtService.generateToken(user)
        );
    }

    // MÉTODOS PRIVADOS
    private GoogleIdToken.Payload validarTokenGoogle(String token) {

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
            )
            .setAudience(List.of(googleClientId))
            .build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token do Google inválido"
                );
            }

            return idToken.getPayload();

        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Erro ao validar token do Google"
            );
        }
    }

    private UserEntity criarUsuarioGoogle(String email, String googleId) {

        return userRepository.save(
            UserEntity.builder()
                .email(email)
                .password(null) // usuário Google não possui senha
                .role(Role.USER)
                .googleId(googleId)
                .build()
        );
    }
}

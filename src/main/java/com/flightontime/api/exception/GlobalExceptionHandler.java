package com.flightontime.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceAccessException.class)
        public ResponseEntity<Map<String, Object>> handleResourceAccess(ResourceAccessException ex) {
                return ResponseEntity
                                .status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(Map.of(
                                                "timestamp", LocalDateTime.now(),
                                                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                                                "erro", "Serviço de predição indisponível. API externa fora do ar."));
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of(
                                                "timestamp", LocalDateTime.now(),
                                                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "erro", ex.getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "timestamp", LocalDateTime.now(),
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "erro", "Erro de validação",
                                                "detalhes", errors));
        }
}

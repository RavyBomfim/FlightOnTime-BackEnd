package com.flightontime.api.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.flightontime.api.integration.prediction.exception.PredictionServiceException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

        //Se a API de predição estiver indisponível ou ocorrer um erro ao chamar o serviço. HTTP 503 – Service Unavailable
        @ExceptionHandler(PredictionServiceException.class)
        public ResponseEntity<Map<String, Object>> handlePredictionService(PredictionServiceException ex) {
                return ResponseEntity
                                .status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(Map.of(
                                                "timestamp", LocalDateTime.now(),
                                                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                                                "erro", ex.getMessage()));
        }

        //Se ocorrer qualquer outro erro inesperado no backend. HTTP 500 – Internal Server Error
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of(
                                                "timestamp", LocalDateTime.now(),
                                                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "erro", "Erro interno no servidor"));

        }

        //Se a validação dos dados de entrada falhar. HTTP 400 – Bad Request
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

        //Se o corpo da requisição for inválido. HTTP 400 – Bad Request
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "timestamp", LocalDateTime.now(),
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "erro", "Corpo da requisição inválido",
                                                "detalhes", "Verifique o formato dos campos enviados"));
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<Object> handleResponseStatus(ResponseStatusException ex) {
                return ResponseEntity
                                .status(ex.getStatusCode())
                                .body(Map.of(
                                                "status", ex.getStatusCode().value(),
                                                "erro", ex.getReason(),
                                                "timestamp", LocalDateTime.now()));
}


}

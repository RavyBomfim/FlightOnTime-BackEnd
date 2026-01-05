package com.flightontime.api.integration.prediction.exception;

public class PredictionServiceException extends RuntimeException {

    public PredictionServiceException(String message) {
        super(message);
    }

    public PredictionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

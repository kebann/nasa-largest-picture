package com.example.exception;

public class NasaServiceException extends RuntimeException {

    public NasaServiceException(String message) {
        super(message);
    }

    public NasaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

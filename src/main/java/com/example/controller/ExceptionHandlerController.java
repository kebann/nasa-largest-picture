package com.example.controller;

import com.example.dto.ErrorResponse;
import com.example.exception.NasaServiceException;
import com.example.exception.PictureNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(PictureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePictureNotFoundException(PictureNotFoundException e) {
        var error = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(NasaServiceException.class)
    public ResponseEntity<ErrorResponse> handleNasaServiceException(NasaServiceException e) {
        var error = new ErrorResponse(e.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}
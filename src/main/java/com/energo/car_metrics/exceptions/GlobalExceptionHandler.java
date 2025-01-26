package com.energo.car_metrics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exp, WebRequest request) {
        return buildErrorResponse(exp, "Invalid username or password", HttpStatus.UNAUTHORIZED, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exp, String message, HttpStatus httpStatus, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", httpStatus.value());
        response.put("error", httpStatus.getReasonPhrase());
        response.put("message", message);
        response.put("path", request.getDescription(false).replace("uri=",""));
        return new ResponseEntity<>(response, httpStatus);
    }
}

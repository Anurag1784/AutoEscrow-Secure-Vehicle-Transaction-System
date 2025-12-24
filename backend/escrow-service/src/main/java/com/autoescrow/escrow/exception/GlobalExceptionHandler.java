package com.autoescrow.escrow.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildResponse(
            HttpStatus status,
            String message,
            String path) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);

        return body;
    }

    // ===============================
    // 404 – Escrow Not Found
    // ===============================
    @ExceptionHandler(EscrowNotFoundException.class)
    public ResponseEntity<?> handleEscrowNotFound(
            EscrowNotFoundException ex,
            jakarta.servlet.http.HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    // ===============================
    // 400 – Invalid Escrow State
    // ===============================
    @ExceptionHandler(InvalidEscrowStateException.class)
    public ResponseEntity<?> handleInvalidState(
            InvalidEscrowStateException ex,
            jakarta.servlet.http.HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    // ===============================
    // 403 – Unauthorized Action
    // ===============================
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<?> handleUnauthorized(
            UnauthorizedActionException ex,
            jakarta.servlet.http.HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.FORBIDDEN,
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.FORBIDDEN
        );
    }

    // ===============================
    // 500 – Fallback (Unexpected)
    // ===============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(
            Exception ex,
            jakarta.servlet.http.HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Something went wrong",
                        request.getRequestURI()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

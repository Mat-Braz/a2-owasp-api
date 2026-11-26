package com.socialconnect.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> status(ResponseStatusException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getReason() == null ? "Erro" : ex.getReason());
        body.put("status", ex.getStatusCode().value());

        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> unhandled(Exception ex, HttpServletRequest request) {
        log.error("Erro não tratado em {}", request.getRequestURI(), ex);

        String uri = request.getRequestURI() == null ? "" : request.getRequestURI();

        if (uri.startsWith("/api/v1")) {
            StringWriter writer = new StringWriter();
            ex.printStackTrace(new PrintWriter(writer));

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", ex.getMessage());
            body.put("exception", ex.getClass().getName());
            body.put("trace", writer.toString());
            body.put("status", 500);

            return ResponseEntity.internalServerError().body(body);
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Erro interno. Tente novamente.");
        body.put("status", 500);

        return ResponseEntity.internalServerError().body(body);
    }
}

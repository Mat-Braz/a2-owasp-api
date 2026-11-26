package com.socialconnect.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();

        if (path == null || !path.startsWith("/api/")) {
            return true;
        }

        return "/api/v1/login".equals(path) || "/api/v2/login".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            write(response, 401, "Token ausente");
            return;
        }

        String token = header.substring(7).trim();

        if (token.isEmpty()) {
            write(response, 401, "Token ausente");
            return;
        }

        try {
            String path = request.getRequestURI();
            AuthUser user = path.startsWith("/api/v1/")
                    ? jwtService.readPayloadOnly(token)
                    : jwtService.parseAndVerify(token);

            request.setAttribute(CurrentUser.ATTRIBUTE, user);
            filterChain.doFilter(request, response);
        } catch (ResponseStatusException ex) {
            String message = ex.getReason() == null ? "Token inválido" : ex.getReason();
            write(response, ex.getStatusCode().value(), message);
        }
    }

    private void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", message);
        body.put("status", status);

        objectMapper.writeValue(response.getWriter(), body);
    }
}

package com.socialconnect.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CurrentUser {

    public static final String ATTRIBUTE = "socialconnect.auth";

    public AuthUser require(HttpServletRequest request) {
        Object value = request.getAttribute(ATTRIBUTE);
        if (value instanceof AuthUser authUser) {
            return authUser;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
    }
}

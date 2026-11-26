package com.socialconnect.security;

public record AuthUser(Long id, String username, String role, long exp) {
}
